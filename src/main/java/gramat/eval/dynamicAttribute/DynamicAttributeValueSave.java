package gramat.eval.dynamicAttribute;

import gramat.eval.Evaluator;
import gramat.eval.SubAction;

public class DynamicAttributeValueSave extends SubAction<DynamicAttributeValueStart> {

    private final DynamicAttributeNameSave savedName;

    public DynamicAttributeValueSave(DynamicAttributeValueStart origin, DynamicAttributeNameSave savedName) {
        super(origin);
        this.savedName = savedName;
    }

    @Override
    public void run(Evaluator evaluator) {
        if (origin.active) {
            evaluator.debugger.log(toString());
            evaluator.debugger.indent(-1);

            if (savedName.name != null) {
                var assembler = evaluator.popAssembler();

                var value = assembler.popValue();

                assembler.expectEmpty();

                evaluator.peekAssembler().setAttribute(savedName.name, value);
            } else {
                System.out.println("Missing name");
            }

            origin.active = false;
        }
    }

    @Override
    public String getDescription() {
        return "COMMIT SET/VALUE";
    }
}
