package org.bakasoft.gramat.diff;

import org.bakasoft.gramat.util.CodeWriter;

public class DiffObject extends Diff {

    private final DiffProperty[] properties;

    public DiffObject(DiffProperty[] properties) {
        this.properties = properties;
    }

    @Override
    public void toString(CodeWriter writer) {
        writer.write('{');
        writer.indent(+1);
        writer.breakLine();

        for (DiffProperty property : properties) {
            property.toString(writer);
            writer.breakLine();
        }

        writer.indent(-1);
        writer.write('}');
    }
}
