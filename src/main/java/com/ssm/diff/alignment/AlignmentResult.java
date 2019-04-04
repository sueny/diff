package com.ssm.diff.alignment;

import java.util.List;

public class AlignmentResult {
    List<Match> matches;
    List<Mismatch> mismatchesS1;
    List<Mismatch> mismatchesS2;

    AlignmentResult(List<Match> matches, List<Mismatch> mismatchesS1, List<Mismatch> mismatchesS2) {
        this.matches = matches;
        this.mismatchesS1 = mismatchesS1;
        this.mismatchesS2 = mismatchesS2;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public List<Mismatch> getMismatchesS1() {
        return mismatchesS1;
    }

    public List<Mismatch> getMismatchesS2() {
        return mismatchesS2;
    }
}
