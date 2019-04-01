package org.bakasoft.gramat.diff;

import org.bakasoft.gramat.util.CodeWriter;

public class DiffProperty extends Diff {

    private final Object key;
    private final Diff diff;

    public DiffProperty(Object key, Diff diff) {
        this.key = key;
        this.diff = diff;
    }

    @Override
    public void toString(CodeWriter writer) {
        writer.write(String.valueOf(key));
        writer.write(": ");
        diff.toString(writer);
    }
}
