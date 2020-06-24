package gramat.eval.dynamicAttribute;

import gramat.eval.TRXAction;
import gramat.eval.Evaluator;

public class DynamicAttributeNameStart extends TRXAction {

    public boolean active;

    @Override
    public void run(Evaluator evaluator) {
        if (!active) {
            evaluator.debugger.log(toString());
            evaluator.debugger.indent(+1);
            evaluator.pushAssembler();
            active = true;
        }
    }

    @Override
    public String getDescription() {
        return "BEGIN SET/NAME";
    }
}
