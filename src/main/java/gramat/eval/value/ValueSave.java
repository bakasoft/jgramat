package gramat.eval.value;

import gramat.compiling.ValueParser;
import gramat.eval.Action;
import gramat.eval.Evaluator;

public class ValueSave extends Action {
    private final Action origin;
    private final ValueParser parser;

    public ValueSave(Action origin, ValueParser parser) {
        this.origin = origin;
        this.parser = parser;
    }

    @Override
    public void run(Evaluator evaluator) {

    }

    @Override
    public String toString() {
        return "Save " + parser + " value";
    }
}
