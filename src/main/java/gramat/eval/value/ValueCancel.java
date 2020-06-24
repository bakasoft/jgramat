package gramat.eval.value;

import gramat.compiling.ValueParser;
import gramat.eval.Evaluator;
import gramat.eval.SubAction;

public class ValueCancel extends SubAction<ValueStart> {

    private final ValueParser parser;

    public ValueCancel(ValueStart origin, ValueParser parser) {
        super(origin);
        this.parser = parser;
    }

    @Override
    public void run(Evaluator evaluator) {
        evaluator.debugger.log(toString());
        evaluator.debugger.indent(-1);
        if (origin.position == null) {
            System.out.println("WARNING: canceling before start @ " + this);
        }

        origin.position = null;
    }

    @Override
    public String getDescription() {
        return "ROLLBACK VALUE";
    }
}
