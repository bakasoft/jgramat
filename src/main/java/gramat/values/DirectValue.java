package gramat.values;

public class DirectValue implements Value {

    private final Object value;

    public DirectValue(Object value) {
        this.value = value;
    }

    @Override
    public Object build() {
        return value;
    }

}
