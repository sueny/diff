package com.ssm.diff.alignment;

class Node {
    int i;
    int j;
    int score;
    Node previous;

    Node(int score, int i, int j) {
        this.score = score;
        this.i = i;
        this.j = j;
    }

    Node(int score, int i, int j, Node previous) {
        this.score = score;
        this.i = i;
        this.j = j;
        this.previous = previous;
    }
}