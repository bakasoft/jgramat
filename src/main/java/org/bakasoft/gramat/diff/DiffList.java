package org.bakasoft.gramat.diff;

import org.bakasoft.gramat.util.CodeWriter;

public class DiffList extends Diff {

    private final int index;
    private final Diff diff;

    public DiffList(int index, Diff diff) {
        this.index = index;
        this.diff = diff;
    }

    @Override
    public void toString(CodeWriter writer) {
        writer.write('[');
        writer.write(String.valueOf(index));
        writer.write("]: ");
        diff.toString(writer);
    }
}
