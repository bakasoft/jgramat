package gramat.eval.dynamicAttribute;

import gramat.eval.Action;
import gramat.eval.Evaluator;
import gramat.eval.SubAction;

public class DynamicAttributeNameCancel extends SubAction<DynamicAttributeNameStart> {
    public DynamicAttributeNameCancel(DynamicAttributeNameStart origin) {
        super(origin);
    }

    @Override
    public void run(Evaluator evaluator) {
        if (origin.active) {
            evaluator.debugger.log(toString());
            evaluator.debugger.indent(-1);
            evaluator.popAssembler();

            origin.active = false;
        }
    }

    @Override
    public String getDescription() {
        return "ROLLBACK SET/NAME";
    }
}
