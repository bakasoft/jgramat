package gramat.scheme.machine.binary;

import stone.binary.schema.IndexedField;

public enum StateField implements IndexedField {
    ID(0),
    ACCEPTED(1),
    ;

    private final int index;

    StateField(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }
}
