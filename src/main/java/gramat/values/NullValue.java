package gramat.values;

import gramat.GramatException;

public class NullValue implements Value {

    @Override
    public Object build() {
        return null;
    }

    @Override
    public void concat(ConcatenatedValue value) {
        throw new GramatException("cannot concatenate nulls");
    }

}
