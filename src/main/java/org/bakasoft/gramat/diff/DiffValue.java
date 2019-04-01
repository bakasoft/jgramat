package org.bakasoft.gramat.diff;

import org.bakasoft.gramat.util.CodeWriter;

public class DiffValue extends Diff {

    private final Object expected;
    private final Object actual;

    public DiffValue(Object expected, Object actual){
        this.expected = expected;
        this.actual = actual;
    }

    @Override
    public void toString(CodeWriter writer) {
        writer.write("Expected value " + expected + " instead of " + actual);
    }
}
