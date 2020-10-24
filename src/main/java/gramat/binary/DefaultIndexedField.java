package gramat.binary;

public class DefaultIndexedField implements IndexedField {
    private final int index;

    public DefaultIndexedField(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }
}
