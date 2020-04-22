package gramat.values;

import gramat.GramatException;

import java.util.ArrayList;
import java.util.List;

abstract public class ListValue implements Value {

    private List<Value> values;

    protected List<Value> getValues() {
        if (values == null) {
            values = new ArrayList<>();
        }
        return values;
    }

    @Override
    public void concat(ConcatenatedValue value) {
        throw new GramatException("cannot concatenate a list");
    }

    public void add(Value value) {
        getValues().add(value);
    }

}
