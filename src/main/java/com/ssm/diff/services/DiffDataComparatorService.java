package com.ssm.diff.services;

import com.ssm.diff.domain.DiffData;
import com.ssm.diff.domain.LeftData;
import com.ssm.diff.domain.RightData;

public interface DiffDataComparatorService {
    DiffData diff(LeftData leftData, RightData rightData);
    void diffAndStore(LeftData leftData, RightData rightData);
}
