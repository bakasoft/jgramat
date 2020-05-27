package gramat.eval.dynamicAttribute;

import gramat.eval.Action;
import gramat.eval.Evaluator;

public class DynamicAttributeValueStart extends Action {

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
        return "BEGIN SET/VALUE";
    }
}
