package gramat.eval.dynamicAttribute;

import gramat.eval.Action;
import gramat.eval.Evaluator;
import gramat.eval.SubAction;

public class DynamicAttributeNameSave extends SubAction {
    public DynamicAttributeNameSave(Action origin) {
        super(origin);
    }

    public String name;

    @Override
    public void run(Evaluator evaluator) {
        var assembler = evaluator.popAssembler();

        name = assembler.popString();

        assembler.expectEmpty();
    }

    @Override
    public String getDescription() {
        return "Save Name Dyn-Attribute";
    }
}
