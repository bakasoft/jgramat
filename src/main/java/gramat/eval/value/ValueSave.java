package gramat.eval.value;

import gramat.compiling.ValueParser;
import gramat.eval.Action;
import gramat.eval.Evaluator;
import gramat.eval.SubAction;

public class ValueSave extends SubAction {

    private final ValueParser parser;

    public ValueSave(Action origin, ValueParser parser) {
        super(origin);
        this.parser = parser;
    }

    @Override
    public void run(Evaluator evaluator) {

    }

    @Override
    public String getDescription() {
        return "Save " + parser + " value";
    }
}
