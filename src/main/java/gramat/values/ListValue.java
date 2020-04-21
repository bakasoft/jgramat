package gramat.values;

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

    public void add(Value value) {
        getValues().add(value);
    }

}
