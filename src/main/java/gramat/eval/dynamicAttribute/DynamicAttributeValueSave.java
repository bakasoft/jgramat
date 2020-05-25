package gramat.eval.dynamicAttribute;

import gramat.eval.Action;
import gramat.eval.Evaluator;
import gramat.eval.SubAction;

public class DynamicAttributeValueSave extends SubAction {

    private final DynamicAttributeNameSave savedName;

    public DynamicAttributeValueSave(Action origin, DynamicAttributeNameSave savedName) {
        super(origin);
        this.savedName = savedName;
    }

    @Override
    public void run(Evaluator evaluator) {
        if (savedName.name == null) {
            throw new RuntimeException();
        }

        var assembler = evaluator.popAssembler();

        var value = assembler.popValue();

        assembler.expectEmpty();

        evaluator.peekAssembler().setAttribute(savedName.name, value);
    }

    @Override
    public String getDescription() {
        return "Save Value Dyn-Attribute";
    }
}
