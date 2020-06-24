package gramat.eval.dynamicAttribute;

import gramat.eval.Evaluator;
import gramat.eval.SubAction;

public class DynamicAttributeNameSave extends SubAction<DynamicAttributeNameStart> {
    public DynamicAttributeNameSave(DynamicAttributeNameStart origin) {
        super(origin);
    }

    public String name;

    @Override
    public void run(Evaluator evaluator) {
        if (origin.active) {
            evaluator.debugger.log(toString());
            evaluator.debugger.indent(-1);
            var assembler = evaluator.popAssembler();

            name = assembler.popString();

            assembler.expectEmpty();

            origin.active = false;
        }
    }

    @Override
    public String getDescription() {
        return "COMMIT SET/NAME";
    }
}
