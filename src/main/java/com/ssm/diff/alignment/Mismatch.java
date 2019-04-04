package com.ssm.diff.alignment;

public class Mismatch {
    int index;
    int length;

    Mismatch(int index) {
        this.index = index;
        this.length = 1;
    }

    void addUp() {
        this.length = this.length + 1;
    }

    public int getIndex() {
        return index;
    }

    public int getLength() {
        return length;
    }
}
