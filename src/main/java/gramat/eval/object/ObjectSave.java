package gramat.eval.object;

import gramat.eval.Action;
import gramat.eval.Evaluator;
import gramat.eval.SubAction;

import java.util.LinkedHashMap;

public class ObjectSave extends SubAction<ObjectStart> {


    public ObjectSave(ObjectStart origin) {
        super(origin);
    }

    @Override
    public String getDescription() {
        return "COMMIT OBJECT";
    }

    @Override
    public void run(Evaluator evaluator) {
        if (origin.active) {
            evaluator.debugger.log(toString());
            evaluator.debugger.indent(-1);
            var assembler = evaluator.popAssembler();

            var object = assembler.getAttributes();  // TODO add types

            evaluator.peekAssembler().pushValue(object);

            origin.active = false;
        }
    }
}
