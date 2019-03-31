package org.bakasoft.gramat.diff;

public class DiffList extends Diff {

    private final int index;
    private final Diff diff;

    public DiffList(int index, Diff diff) {
        this.index = index;
        this.diff = diff;
    }

}
