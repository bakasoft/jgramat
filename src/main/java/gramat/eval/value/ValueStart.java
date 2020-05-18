package gramat.eval.value;

import gramat.compiling.ValueParser;
import gramat.eval.Action;
import gramat.eval.Evaluator;

public class ValueStart extends Action {

    private final ValueParser parser;

    public ValueStart(ValueParser parser) {
        this.parser = parser;
    }

    @Override
    public void run(Evaluator evaluator) {

    }

    @Override
    public String getDescription() {
        return "Start " + parser + " value";
    }
}
