package gramat.eval.value;

import gramat.compiling.ValueParser;
import gramat.eval.TRXAction;
import gramat.eval.Evaluator;

public class ValueStart extends TRXAction {

    private final ValueParser parser;

    public Integer position;

    public ValueStart(ValueParser parser) {
        this.parser = parser;
    }

    @Override
    public void run(Evaluator evaluator) {
        evaluator.debugger.log(toString());
        evaluator.debugger.indent(+1);
        if (position != null) {
            System.out.println("WARNING: re-starting @ " + this);
        }

        position = evaluator.source.getPosition();
    }

    @Override
    public String getDescription() {
        return "BEGIN VALUE";
    }
}
