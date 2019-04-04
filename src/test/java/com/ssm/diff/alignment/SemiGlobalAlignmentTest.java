package com.ssm.diff.alignment;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SemiGlobalAlignmentTest {

    private SemiGlobalAlignment alignment = new SemiGlobalAlignment();

    @Test
    public void alignEqualStrings() {
        AlignmentResult alignmentResult = alignment.alignStrings("111", "111");

        assertEquals(1, alignmentResult.matches.size());
        assertMatch(1, 1, 3, alignmentResult.matches.get(0));
        assertEquals(0, alignmentResult.mismatchesS1.size());
        assertEquals(0, alignmentResult.mismatchesS2.size());
    }

    @Test
    public void alignDifferentStrings() {
        AlignmentResult alignmentResult = alignment.alignStrings("000", "111");
        assertEquals(0, alignmentResult.matches.size());
        assertEquals(1, alignmentResult.mismatchesS1.size());
        assertMismatch(1, 3, alignmentResult.mismatchesS1.get(0));
        assertEquals(1, alignmentResult.mismatchesS2.size());
        assertMismatch(1, 3, alignmentResult.mismatchesS2.get(0));
    }

    @Test
    public void alignEmptyStrings() {
        AlignmentResult alignmentResult = alignment.alignStrings("", "");

        assertEquals(0, alignmentResult.matches.size());
        assertEquals(0, alignmentResult.mismatchesS1.size());
        assertEquals(0, alignmentResult.mismatchesS2.size());
    }

    @Test
    public void alignStringsPrioritizeMaximumScore() {
        AlignmentResult alignmentResult = alignment.alignStrings("AAABCDE", "BCDEAAA");

        assertEquals(1, alignmentResult.matches.size());
        assertMatch(4, 1, 4, alignmentResult.matches.get(0));
        assertEquals(1, alignmentResult.mismatchesS1.size());
        assertMismatch(1, 3, alignmentResult.mismatchesS1.get(0));
        assertEquals(1, alignmentResult.mismatchesS2.size());
        assertMismatch(5, 3, alignmentResult.mismatchesS2.get(0));

    }

    @Test
    public void alignStringsWithManyAlignments() {
        AlignmentResult alignmentResult = alignment.alignStrings("XAAAXBBBXCCC", "AAAZBBBZCCCZ");
        assertEquals(3, alignmentResult.matches.size());
        assertMatch(2, 1, 3, alignmentResult.matches.get(0));
        assertMatch(6, 5, 3, alignmentResult.matches.get(1));
        assertMatch(10, 9, 3, alignmentResult.matches.get(2));

        assertEquals(3, alignmentResult.mismatchesS1.size());
        assertMismatch(1, 1, alignmentResult.mismatchesS1.get(0));
        assertMismatch(5, 1, alignmentResult.mismatchesS1.get(1));
        assertMismatch(9, 1, alignmentResult.mismatchesS1.get(2));

        assertEquals(3, alignmentResult.mismatchesS2.size());
        assertMismatch(4, 1, alignmentResult.mismatchesS2.get(0));
        assertMismatch(8, 1, alignmentResult.mismatchesS2.get(1));
        assertMismatch(12, 1, alignmentResult.mismatchesS2.get(2));
    }

    private void assertMatch(int expectedIndexS1, int expectedIndexS2, int expectedLength, Match match) {
        assertEquals(expectedIndexS1, match.x);
        assertEquals(expectedIndexS2, match.y);
        assertEquals(expectedLength, match.length);
    }

    private void assertMismatch(int expectedIndex, int expectedLength, Mismatch mismatch) {
        assertEquals(expectedIndex, mismatch.index);
        assertEquals(expectedLength, mismatch.length);
    }
}