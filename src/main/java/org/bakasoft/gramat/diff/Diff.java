package org.bakasoft.gramat.diff;

import org.bakasoft.gramat.util.CodeWriter;

abstract public class Diff {

    abstract public void toString(CodeWriter writer);

    @Override
    public String toString() {
        CodeWriter writer = new CodeWriter();

        toString(writer);

        return writer.toString();
    }
}
