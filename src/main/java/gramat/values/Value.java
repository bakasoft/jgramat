package gramat.values;

public interface Value {

    Object build();

    void concat(ConcatenatedValue value);

}
