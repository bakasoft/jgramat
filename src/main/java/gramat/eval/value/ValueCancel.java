package gramat.eval.value;

import gramat.compiling.ValueParser;
import gramat.eval.Action;
import gramat.eval.Evaluator;
import gramat.eval.SubAction;

public class ValueCancel extends SubAction {

    private final ValueParser parser;

    public ValueCancel(Action origin, ValueParser parser) {
        super(origin);
        this.parser = parser;
    }

    @Override
    public void run(Evaluator evaluator) {

    }

    @Override
    public String getDescription() {
        return "Cancel `" + parser + "` value";
    }
}
