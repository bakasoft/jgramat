package gramat.eval.value;

import gramat.compiling.ValueParser;
import gramat.eval.Action;
import gramat.eval.Evaluator;

public class ValueStart extends Action {

    private final ValueParser parser;

    public Integer position;

    public ValueStart(ValueParser parser) {
        this.parser = parser;
    }

    @Override
    public void run(Evaluator evaluator) {
        if (position != null) {
            System.out.println("WARNING: re-starting @ " + this);
        }

        position = evaluator.source.getPosition();
    }

    @Override
    public String getDescription() {
        return "Start " + parser + " value";
    }
}
