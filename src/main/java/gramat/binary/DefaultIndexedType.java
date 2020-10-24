package gramat.binary;

public class DefaultIndexedType implements IndexedType {
    private final int index;

    public DefaultIndexedType(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }
}
