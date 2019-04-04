package com.ssm.diff.services;

import com.ssm.diff.domain.DiffData;
import com.ssm.diff.domain.LeftData;
import com.ssm.diff.domain.RightData;
import com.ssm.diff.exceptions.NotFoundException;
import com.ssm.diff.repositories.DiffDataRepository;
import com.ssm.diff.repositories.LeftDataRepository;
import com.ssm.diff.repositories.RightDataRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DiffDataServiceImplTest {

    private static final long ID = 1L;
    private static final String DATA = "DATA_CONTENT";

    @InjectMocks
    private DiffDataServiceImpl diffDataService;
    @Mock
    private LeftDataRepository leftDataRepository;
    @Mock
    private RightDataRepository rightDataRepository;
    @Mock
    private DiffDataRepository diffDataRepository;
    @Mock
    private DiffDataComparatorService diffDataComparatorService;

    @Test
    public void testSaveLeftData() {
        LeftData leftData = new LeftData(ID, DATA);
        when(leftDataRepository.save(leftData)).thenReturn(leftData);

        diffDataService.saveLeftData(ID, leftData);

        verify(leftDataRepository).save(leftData);
        verifyZeroInteractions(diffDataComparatorService);
    }

    @Test
    public void testSaveLeftData_ComputeDiffWhenRightIsAlreadySaved() {
        LeftData leftData = new LeftData(ID, DATA);
        when(leftDataRepository.save(leftData)).thenReturn(leftData);
        RightData rightData = new RightData();
        when(rightDataRepository.findById(ID)).thenReturn(Optional.of(rightData));

        diffDataService.saveLeftData(ID, leftData);

        verify(leftDataRepository).save(leftData);
        verify(diffDataComparatorService).diffAndStore(leftData, rightData);
    }

    @Test
    public void testSaveRightData() {
        RightData rightData = new RightData(ID, DATA);
        when(rightDataRepository.save(rightData)).thenReturn(rightData);

        diffDataService.saveRightData(ID, rightData);

        verify(rightDataRepository).save(rightData);
        verifyZeroInteractions(diffDataComparatorService);
    }

    @Test
    public void testSaveRightData_ComputeDiffWhenLeftIsAlreadySaved() {
        RightData rightData = new RightData(ID, DATA);
        when(rightDataRepository.save(rightData)).thenReturn(rightData);
        LeftData leftData = new LeftData();
        when(leftDataRepository.findById(ID)).thenReturn(Optional.of(leftData));

        diffDataService.saveRightData(ID, rightData);

        verify(rightDataRepository).save(rightData);
        verify(diffDataComparatorService).diffAndStore(leftData, rightData);
    }

    @Test
    public void testGetDiff() {
        when(leftDataRepository.findById(ID)).thenReturn(Optional.of(new LeftData()));
        when(rightDataRepository.findById(ID)).thenReturn(Optional.of(new RightData()));
        DiffData diffData = new DiffData();
        when(diffDataRepository.findById(ID)).thenReturn(Optional.of(diffData));

        DiffData diff = diffDataService.getDiff(ID);

        assertEquals(diffData, diff);
    }

    @Test(expected = NotFoundException.class)
    public void testGetDiff_ShouldThrowNotFoundWhenLeftIsMissing() {
        when(leftDataRepository.findById(ID)).thenReturn(Optional.empty());

        diffDataService.getDiff(ID);
    }

    @Test(expected = NotFoundException.class)
    public void testGetDiff_ShouldThrowNotFoundWhenRightIsMissing() {
        when(leftDataRepository.findById(ID)).thenReturn(Optional.of(new LeftData()));
        when(rightDataRepository.findById(ID)).thenReturn(Optional.empty());

        diffDataService.getDiff(ID);
    }

    @Test
    public void testGetDiff_ShouldCallDiffWhenDiffDataIsMissing() {
        LeftData leftData = new LeftData();
        when(leftDataRepository.findById(ID)).thenReturn(Optional.of(leftData));
        RightData rightData = new RightData();
        when(rightDataRepository.findById(ID)).thenReturn(Optional.of(rightData));
        when(diffDataRepository.findById(ID)).thenReturn(Optional.empty());
        DiffData diffData = new DiffData();
        when(diffDataComparatorService.diff(leftData, rightData)).thenReturn(diffData);

        DiffData diff = diffDataService.getDiff(ID);

        assertEquals(diffData, diff);
    }
}