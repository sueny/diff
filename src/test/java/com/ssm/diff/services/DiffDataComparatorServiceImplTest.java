package com.ssm.diff.services;

import com.ssm.diff.alignment.AlignmentResult;
import com.ssm.diff.alignment.Match;
import com.ssm.diff.alignment.Mismatch;
import com.ssm.diff.alignment.StringAlignment;
import com.ssm.diff.domain.DiffData;
import com.ssm.diff.domain.LeftData;
import com.ssm.diff.domain.RightData;
import com.ssm.diff.repositories.DiffDataRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DiffDataComparatorServiceImplTest {

    private static final long DATA_ID = 1L;
    private static final String DATA_SAMPLE_L3 = "ABC";
    private static final String DATA_SAMPLE_L4 = "ABCD";
    private static final String DATA_SAMPLE_L5_1 = "ABCDE";
    private static final String DATA_SAMPLE_L5_2 = "EDCBA";
    private static final int MATCH1_X = 1;
    private static final int MATCH1_Y = 2;
    private static final int MATCH1_LENGTH = 2;
    private static final int MISMATCH_S1_INDEX = 3;
    private static final int MISMATCH_S1_LENGTH = 4;
    private static final int MISMATCH_S2_INDEX = 5;
    private static final int MISMATCH_S2_LENGTH = 6;

    @InjectMocks
    private DiffDataComparatorServiceImpl diffDataComparatorService;
    @Mock
    private StringAlignment alignment;
    @Mock
    private DiffDataRepository diffDataRepository;

    @Test
    public void testDiff_WhenStringsAreEqual() {
        LeftData leftData = new LeftData(DATA_ID, DATA_SAMPLE_L4);
        RightData rightData = new RightData(DATA_ID, DATA_SAMPLE_L4);

        DiffData diff = diffDataComparatorService.diff(leftData, rightData);

        assertEquals(leftData.getId(), diff.getId());
        assertTrue(diff.getEqual());
        assertNull(diff.getLeftDataSize());
        assertNull(diff.getRightDataSize());
        assertEquals(0, diff.getMatches().size());
        assertEquals(0, diff.getLeftMismatches().size());
        assertEquals(0, diff.getRightMismatches().size());
    }

    @Test
    public void testDiff_WhenStringsHaveDifferentLength() {
        LeftData leftData = new LeftData(DATA_ID, DATA_SAMPLE_L3);
        RightData rightData = new RightData(DATA_ID, DATA_SAMPLE_L4);

        DiffData diff = diffDataComparatorService.diff(leftData, rightData);

        assertEquals(leftData.getId(), diff.getId());
        assertNull(diff.getEqual());
        assertEquals(3, diff.getLeftDataSize().intValue());
        assertEquals(4, diff.getRightDataSize().intValue());
        assertEquals(0, diff.getMatches().size());
        assertEquals(0, diff.getLeftMismatches().size());
        assertEquals(0, diff.getRightMismatches().size());
    }

    @Test
    public void testDiff_WhenStringsHaveSameLengthButAreDifferent() {
        LeftData leftData = new LeftData(DATA_ID, DATA_SAMPLE_L5_1);
        RightData rightData = new RightData(DATA_ID, DATA_SAMPLE_L5_2);

        Match match = mockMatch(MATCH1_X, MATCH1_Y, MATCH1_LENGTH);
        Mismatch mismatchS1 = mockMismatch(MISMATCH_S1_INDEX, MISMATCH_S1_LENGTH);
        Mismatch mismatchS2 = mockMismatch(MISMATCH_S2_INDEX, MISMATCH_S2_LENGTH);
        AlignmentResult alignmentResult =
                mockAlignmentResult(singletonList(match), singletonList(mismatchS1), singletonList(mismatchS2));
        when(alignment.alignStrings(leftData.getData(), rightData.getData())).thenReturn(alignmentResult);

        DiffData diff = diffDataComparatorService.diff(leftData, rightData);

        assertEquals(leftData.getId(), diff.getId());
        assertNull(diff.getEqual());
        assertNull(diff.getLeftDataSize());
        assertNull(diff.getRightDataSize());
        assertEquals(1, diff.getMatches().size());
        assertEquals(MATCH1_X - 1, diff.getMatches().get(0).getLeftIndex().intValue());
        assertEquals(MATCH1_Y - 1, diff.getMatches().get(0).getRightIndex().intValue());
        assertEquals(MATCH1_LENGTH, diff.getMatches().get(0).getLength().intValue());
        assertEquals(1, diff.getLeftMismatches().size());
        assertEquals(MISMATCH_S1_INDEX - 1, diff.getLeftMismatches().get(0).getIndex().intValue());
        assertEquals(MISMATCH_S1_LENGTH, diff.getLeftMismatches().get(0).getLength().intValue());
        assertEquals(1, diff.getRightMismatches().size());
        assertEquals(MISMATCH_S2_INDEX - 1, diff.getRightMismatches().get(0).getIndex().intValue());
        assertEquals(MISMATCH_S2_LENGTH, diff.getRightMismatches().get(0).getLength().intValue());
    }

    @Test
    public void testDiffAndStore() {
        LeftData leftData = new LeftData(DATA_ID, DATA_SAMPLE_L5_1);
        RightData rightData = new RightData(DATA_ID, DATA_SAMPLE_L5_2);

        AlignmentResult mockAlignmentResult = mockAlignmentResult(emptyList(), emptyList(), emptyList());
        when(alignment.alignStrings(leftData.getData(), rightData.getData())).thenReturn(mockAlignmentResult);

        diffDataComparatorService.diffAndStore(leftData, rightData);

        verify(diffDataRepository).save(any(DiffData.class));
    }

    private Match mockMatch(int x, int y, int length) {
        Match match = mock(Match.class);
        when(match.getX()).thenReturn(x);
        when(match.getY()).thenReturn(y);
        when(match.getLength()).thenReturn(length);
        return match;
    }

    private Mismatch mockMismatch(int index, int length) {
        Mismatch mismatch = mock(Mismatch.class);
        when(mismatch.getIndex()).thenReturn(index);
        when(mismatch.getLength()).thenReturn(length);
        return mismatch;
    }

    private AlignmentResult mockAlignmentResult(List<Match> matches, List<Mismatch> mismatchesS1, List<Mismatch> mismatchesS2) {
        AlignmentResult alignmentResult = mock(AlignmentResult.class);
        when(alignmentResult.getMatches()).thenReturn(matches);
        when(alignmentResult.getMismatchesS1()).thenReturn(mismatchesS1);
        when(alignmentResult.getMismatchesS2()).thenReturn(mismatchesS2);
        return alignmentResult;
    }
}