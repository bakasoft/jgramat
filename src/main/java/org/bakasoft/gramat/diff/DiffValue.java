package org.bakasoft.gramat.diff;

public class DiffValue extends Diff {

    private final Object left;
    private final Object right;

    public DiffValue(Object left, Object right){
        this.left = left;
        this.right = right;
    }

}
