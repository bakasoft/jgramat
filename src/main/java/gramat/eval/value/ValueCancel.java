package gramat.eval.value;

import gramat.compiling.ValueParser;
import gramat.eval.Action;
import gramat.eval.Evaluator;

public class ValueCancel extends Action {

    private final Action origin;
    private final ValueParser parser;

    public ValueCancel(Action origin, ValueParser parser) {
        this.origin = origin;
        this.parser = parser;
    }

    @Override
    public void run(Evaluator evaluator) {

    }

    @Override
    public String toString() {
        return "Cancel `" + parser + "` value";
    }
}
