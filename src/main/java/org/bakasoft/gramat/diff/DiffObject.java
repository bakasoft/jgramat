package org.bakasoft.gramat.diff;

public class DiffObject extends Diff {

    private final DiffProperty[] properties;

    public DiffObject(DiffProperty[] properties) {
        this.properties = properties;
    }

}
