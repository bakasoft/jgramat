package gramat.values;

import gramat.GramatException;

import java.util.ArrayList;

public class StringJoin implements StringValue, ContainerValue {

    private final ArrayList<StringValue> items;

    public StringJoin() {
        items = new ArrayList<>();
    }

    @Override
    public Object build() {
        StringBuilder output = new StringBuilder();

        for (var item : items) {
            output.append(item.toString());
        }

        return output.toString();
    }

    @Override
    public void add(Value item) {
        if (item instanceof StringValue) {
            items.add((StringValue)item);
        }
        else {
            throw new GramatException("expected string value to join");
        }
    }
}
