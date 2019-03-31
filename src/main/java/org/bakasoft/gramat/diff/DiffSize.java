package org.bakasoft.gramat.diff;

public class DiffSize extends Diff {

    private final int rightSize;
    private final int leftSize;

    public DiffSize(int rightSize, int leftSize) {
        this.rightSize = rightSize;
        this.leftSize = leftSize;
    }
}
