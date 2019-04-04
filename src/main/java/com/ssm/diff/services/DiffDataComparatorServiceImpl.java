package com.ssm.diff.services;

import com.ssm.diff.alignment.AlignmentResult;
import com.ssm.diff.alignment.Match;
import com.ssm.diff.alignment.Mismatch;
import com.ssm.diff.alignment.StringAlignment;
import com.ssm.diff.domain.DiffData;
import com.ssm.diff.domain.LeftData;
import com.ssm.diff.domain.MatchOffset;
import com.ssm.diff.domain.MismatchOffset;
import com.ssm.diff.domain.RightData;
import com.ssm.diff.repositories.DiffDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DiffDataComparatorServiceImpl implements DiffDataComparatorService {

    private static final Function<Match, MatchOffset> MATCH_TO_OFFSET_PARSER =
            match -> new MatchOffset(match.getX() - 1, match.getY() - 1, match.getLength());
    private static final Function<Mismatch, MismatchOffset> MISMATCH_TO_OFFSET_PARSER =
            mismatch -> new MismatchOffset(mismatch.getIndex() - 1, mismatch.getLength());
    private StringAlignment alignment;
    private DiffDataRepository diffDataRepository;

    @Autowired
    public DiffDataComparatorServiceImpl(StringAlignment alignment, DiffDataRepository diffDataRepository) {
        this.alignment = alignment;
        this.diffDataRepository = diffDataRepository;
    }

    @Async
    public void diffAndStore(LeftData leftData, RightData rightData) {
        DiffData diffData = diff(leftData, rightData);
        diffDataRepository.save(diffData);
    }

    /**
     * Provides information whether left and right data are equals, or their length in case they are different.
     * If they have the same size but are different, then a list of matches and mismatches are returned.
     * @param leftData
     * @param rightData
     * @return DiffData .It returns DiffData with the field equal = true, if the left and right data are equal.
     * If left and right Data have different length, then both length are returned.
     * Otherwise, the left and right data are aligned, and a list of matches and mismatches are returned.
     */
    public DiffData diff(LeftData leftData, RightData rightData) {
        boolean leftAndRightAreEqual = leftData.getData().equals(rightData.getData());
        if (leftAndRightAreEqual) {
            return new DiffData(leftData.getId(), Boolean.TRUE);
        }

        boolean mismatchLeftAndRightLength = leftData.getData().length() != rightData.getData().length();
        if (mismatchLeftAndRightLength) {
            return new DiffData(leftData.getId(), leftData.getData().length(), rightData.getData().length());
        }

        AlignmentResult alignmentResult = alignment.alignStrings(leftData.getData(), rightData.getData());
        return parseAlignmentResultToDiffData(leftData.getId(), alignmentResult);
    }

    /**
     * Parse the AlignmentResult to the entity DiffData.
     * The parse functions subtract 1 unit from the indexes in order to transform a index into a offset.
     */
    private DiffData parseAlignmentResultToDiffData(Long id, AlignmentResult alignmentResult) {
        List<MatchOffset> matches = alignmentResult.getMatches().stream()
                .map(MATCH_TO_OFFSET_PARSER)
                .collect(Collectors.toList());
        List<MismatchOffset> leftMismatches = alignmentResult.getMismatchesS1().stream()
                .map(MISMATCH_TO_OFFSET_PARSER)
                .collect(Collectors.toList());
        List<MismatchOffset> rightMismatches = alignmentResult.getMismatchesS2().stream()
                .map(MISMATCH_TO_OFFSET_PARSER)
                .collect(Collectors.toList());
        return new DiffData(id, matches, leftMismatches, rightMismatches);
    }
}
