package gramat.eval.object;

import gramat.eval.Action;
import gramat.eval.Evaluator;
import gramat.eval.SubAction;

public class ObjectCancel extends SubAction<ObjectStart> {

    public ObjectCancel(ObjectStart origin) {
        super(origin);
    }

    @Override
    public String getDescription() {
        return "ROLLBACK OBJECT";
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
}
