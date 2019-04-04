package com.ssm.diff.alignment;

public class Match {
    int x;
    int y;
    int length;

    Match(int x, int y) {
        this.x = x;
        this.y = y;
        this.length = 1;
    }

    void addUp() {
        this.length = this.length + 1;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLength() {
        return length;
    }

}
