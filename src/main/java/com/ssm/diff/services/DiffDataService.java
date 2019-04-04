package com.ssm.diff.services;

import com.ssm.diff.domain.DiffData;
import com.ssm.diff.domain.LeftData;
import com.ssm.diff.domain.RightData;
import com.ssm.diff.exceptions.NotFoundException;

public interface DiffDataService {
    LeftData saveLeftData(Long id, LeftData data);

    RightData saveRightData(Long id, RightData data);

    DiffData getDiff(Long id) throws NotFoundException;
}
