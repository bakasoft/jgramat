package gramat.values;

import gramat.GramatException;

import java.util.ArrayList;
import java.util.List;

abstract public class ListValue implements ContainerValue {

    private List<Value> values;

    protected List<Value> getValues() {
        if (values == null) {
            values = new ArrayList<>();
        }
        return values;
    }

    public void add(Value value) {
        getValues().add(value);
    }

}
