package org.bakasoft.gramat.diff;

public class DiffException extends RuntimeException {

    private final Diff diff;

    public DiffException(Diff diff) {
        super(diff.toString());
        this.diff = diff;
    }

}
