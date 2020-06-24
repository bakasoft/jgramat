package gramat.eval.object;

import gramat.eval.TRXAction;
import gramat.eval.Evaluator;

public class ObjectStart extends TRXAction {

    public boolean active = true;

    @Override
    public String getDescription() {
        return "BEGIN OBJECT";
    }

    @Override
    public void run(Evaluator evaluator) {
        if (!active) {
            evaluator.debugger.log(toString());
            evaluator.debugger.indent(+1);
            evaluator.pushAssembler();
            active = true;
        }
    }
}
