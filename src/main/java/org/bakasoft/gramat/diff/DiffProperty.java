package org.bakasoft.gramat.diff;

public class DiffProperty extends Diff {

    private final Object key;
    private final Diff diff;

    public DiffProperty(Object key, Diff diff) {
        this.key = key;
        this.diff = diff;
    }

}
