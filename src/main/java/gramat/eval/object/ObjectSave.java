package gramat.eval.object;

import gramat.eval.Action;
import gramat.eval.Evaluator;

import java.util.LinkedHashMap;

public class ObjectSave extends Action {

    @Override
    public String getDescription() {
        return "Object-Commit";
    }

    @Override
    public void run(Evaluator evaluator) {
        var assembler = evaluator.popAssembler();

        var object = assembler.getAttributes();  // TODO add types

        evaluator.peekAssembler().pushValue(object);
    }
}
