package gramat.values;

import gramat.GramatException;

import java.util.ArrayList;
import java.util.Objects;

public class ConcatenatedValue implements Value {

    private final ArrayList<String> values;
    private String parser;

    public ConcatenatedValue() {
        this.values = new ArrayList<>();
    }

    public void add(String value, String parser) {
        if (this.parser == null) {
            this.parser = parser;
        }
        else if (!Objects.equals(this.parser, parser)) {
            throw new GramatException("expected " + this.parser + " instead of " + parser);
        }

        this.values.add(value);
    }

    @Override
    public Object build() {
        StringBuilder output = new StringBuilder();

        for (var value : values) {
            output.append(value);
        }

        return output.toString();
    }

    @Override
    public void concat(ConcatenatedValue value) {
        value.values.addAll(this.values);
    }
}
