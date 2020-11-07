package stone.binary.schema.impl;

import gramat.util.PP;
import stone.binary.schema.IndexedField;

public class DefaultIndexedField implements IndexedField {
    private final int index;

    public DefaultIndexedField(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return String.format("Field<%s>", PP.hex(index));
    }
}
