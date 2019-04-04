package com.ssm.diff.services;

import com.ssm.diff.domain.DiffData;
import com.ssm.diff.domain.LeftData;
import com.ssm.diff.domain.RightData;
import com.ssm.diff.exceptions.NotFoundException;
import com.ssm.diff.repositories.DiffDataRepository;
import com.ssm.diff.repositories.LeftDataRepository;
import com.ssm.diff.repositories.RightDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DiffDataServiceImpl implements DiffDataService {

    private LeftDataRepository leftDataRepository;
    private RightDataRepository rightDataRepository;
    private DiffDataRepository diffDataRepository;
    private DiffDataComparatorService diffDataComparatorService;

    @Autowired
    public DiffDataServiceImpl(LeftDataRepository leftDataRepository, RightDataRepository rightDataRepository,
                               DiffDataRepository diffDataRepository, DiffDataComparatorService diffDataComparatorService) {
        this.leftDataRepository = leftDataRepository;
        this.rightDataRepository = rightDataRepository;
        this.diffDataRepository = diffDataRepository;
        this.diffDataComparatorService = diffDataComparatorService;
    }

    @Override
    public LeftData saveLeftData(Long id, LeftData data) {
        data.setId(id);
        data = leftDataRepository.save(data);
        saveDiffIfBothDataAreStored(data);
        return data;
    }

    @Override
    public RightData saveRightData(Long id, RightData data) {
        data.setId(id);
        data = rightDataRepository.save(data);
        saveDiffIfBothDataAreStored(data);
        return data;
    }

    @Override
    public DiffData getDiff(Long id) throws NotFoundException {
        LeftData leftData = leftDataRepository.findById(id).orElseThrow(NotFoundException::new);
        RightData rightData = rightDataRepository.findById(id).orElseThrow(NotFoundException::new);

        Optional<DiffData> diffData = diffDataRepository.findById(id);
        return diffData.orElse(diffDataComparatorService.diff(leftData, rightData));
    }

    private void saveDiffIfBothDataAreStored(LeftData leftData) {
        Optional<RightData> rightData = rightDataRepository.findById(leftData.getId());
        if (!rightData.isPresent()) {
            return;
        }
        diffDataComparatorService.diffAndStore(leftData, rightData.get());
    }

    private void saveDiffIfBothDataAreStored(RightData rightData) {
        Optional<LeftData> leftData = leftDataRepository.findById(rightData.getId());
        if (!leftData.isPresent()) {
            return;
        }
        diffDataComparatorService.diffAndStore(leftData.get(), rightData);
    }
}
