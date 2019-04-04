package com.ssm.diff.alignment;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.max;
import static java.util.Comparator.comparingInt;

/**
 * Implements Needleman-Wunsch Semi global alignment algorithm to analyse the similarity of two strings.
 */
@Component
public class SemiGlobalAlignment implements StringAlignment {

    private final int MATCH_SCORE = 1;
    private final int MISMATCH_SCORE = Integer.MIN_VALUE;
    private final int GAP_SCORE = 0;

    /**
     * Runs the Needleman-Wunsch alignment algorithm
     * and returns a list of indexes that a match was found, with it respective length,
     * and a list of indexes of string1 that does not match any index at string2.
     *
     * @param s1 string1
     * @param s2 string2
     * @return List of matches indexes and lengths, and list of mismatches indexes and lengths for both strings.
     */
    public AlignmentResult alignStrings(String s1, String s2) {
        String string1 = " " + s1;
        String string2 = " " + s2;

        Node[][] matrix = initMatrix(string1.length(), string2.length());

        //Runs the Needleman-Wunsch algorithm on every node in the matrix, and sets the alignment score.
        for (int i = 1; i < string1.length(); i++) {
            for (int j = 1; j < string2.length(); j++) {
                matrix[i][j] = match(matrix, string1, string2, i, j);
            }
        }

        return traceback(matrix, string1, string2);

    }

    /**
     * Initialize the matrix and build the score on the first row and the first column.
     *
     * @param lengthString1
     * @param lengthString2
     * @return matrix
     */
    private Node[][] initMatrix(int lengthString1, int lengthString2) {
        Node[][] matrix = new Node[lengthString1][lengthString2];
        matrix[0][0] = new Node(GAP_SCORE, 0, 0);
        for (int i = 1; i < lengthString1; i++) {
            matrix[i][0] = new Node(0, i, 0, matrix[i - 1][0]);
        }

        for (int i = 1; i < lengthString2; i++) {
            matrix[0][i] = new Node(0, 0, i, matrix[0][i - 1]);
        }
        return matrix;
    }

    private int score(String string1, String string2, int i, int j) {
        return string1.charAt(i) == string2.charAt(j) ? MATCH_SCORE : MISMATCH_SCORE;
    }

    private Node match(Node[][] matrix, String string1, String string2, int i, int j) {
        int previousDiagonalPlusNewScore = matrix[i - 1][j - 1].score + score(string1, string2, i, j);
        int previousRowPlusGap = matrix[i - 1][j].score + GAP_SCORE;
        int previousColumnPlusGap = matrix[i][j - 1].score + GAP_SCORE;

        int largestScore = max(asList(previousDiagonalPlusNewScore, previousRowPlusGap, previousColumnPlusGap));
        Node largest = new Node(largestScore, i, j);
        if (previousDiagonalPlusNewScore == largest.score) {
            largest.previous = matrix[i - 1][j - 1];
        } else if (previousRowPlusGap == largest.score) {
            largest.previous = matrix[i - 1][j];

        } else {
            largest.previous = matrix[i][j - 1];
        }

        return largest;
    }

    /**
     * Traceback from the last node in the matrix back to the first gap node.
     */
    private AlignmentResult traceback(Node[][] matrix, String string1, String string2) {
        List<Match> matches = new ArrayList<>();
        List<Mismatch> mismatchesS1 = new ArrayList<>();
        List<Mismatch> mismatchesS2 = new ArrayList<>();

        Node current = matrix[string1.length() - 1][string2.length() - 1];
        while (current != null && !(current.i == 0 && current.j == 0)) {
            if (pathLeadsDiagonal(current)) {
                if (string1.charAt(current.i) == string2.charAt(current.j)) {
                    addMatch(matches, current);
                } else {
                    addMismatch(mismatchesS1, current.i);
                    addMismatch(mismatchesS2, current.j);
                }
            } else if (pathLeadsUp(current)) {
                addMismatch(mismatchesS1, current.i);
            } else if (pathLeadsLeft(current)) {
                addMismatch(mismatchesS2, current.j);
            }

            current = current.previous;
        }

        matches.sort(comparingInt(o -> o.x));
        mismatchesS1.sort(comparingInt(o -> o.index));
        mismatchesS2.sort(comparingInt(o -> o.index));
        return new AlignmentResult(matches, mismatchesS1, mismatchesS2);
    }

    private void addMatch(List<Match> matches, Node current) {
        if (!matches.isEmpty() && isLastMatchPreviousOfCurrentNode(matches, current)) {
            Match lastMatch = matches.get(matches.size() - 1);
            lastMatch.x = current.i;
            lastMatch.y = current.j;
            lastMatch.addUp();
        } else {
            matches.add(new Match(current.i, current.j));
        }
    }

    private void addMismatch(List<Mismatch> mismatch, int index) {
        if (!mismatch.isEmpty() && isLastMismatchNextToCurrentIndex(mismatch, index)) {
            Mismatch lastMismatchS1 = mismatch.get(mismatch.size() - 1);
            lastMismatchS1.index = index;
            lastMismatchS1.addUp();
        } else {
            mismatch.add(new Mismatch(index));
        }
    }

    private boolean isLastMatchPreviousOfCurrentNode(List<Match> matches, Node current) {
        Match lastMatch = matches.get(matches.size() - 1);
        return lastMatch.x == current.i + 1 && lastMatch.y == current.j + 1;
    }

    private boolean isLastMismatchNextToCurrentIndex(List<Mismatch> mismatchesS2, int currentIndex) {
        return mismatchesS2.get(mismatchesS2.size() - 1).index == currentIndex + 1;
    }

    private boolean pathLeadsLeft(Node current) {
        return current.j - current.previous.j == 1;
    }

    private boolean pathLeadsUp(Node current) {
        return current.i - current.previous.i == 1;
    }

    private boolean pathLeadsDiagonal(Node current) {
        return current.i - current.previous.i == 1 && current.j - current.previous.j == 1;
    }
}
