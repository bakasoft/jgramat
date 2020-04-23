package gramat.values;

import gramat.GramatException;

import java.util.ArrayList;

public class StringJoin implements StringValue, ContainerValue {

    private final ArrayList<StringValue> items;

    public StringJoin() {
        items = new ArrayList<>();
    }

    @Override
    public String build() {
        StringBuilder output = new StringBuilder();

        for (var item : items) {
            output.append(item.buildString());
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

    @Override
    public String buildString() {
        return build();
    }
}
