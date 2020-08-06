package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class AttributeNameRelease extends CapturingAction {

    private final AttributeNamePress press;

    public String name;

    public AttributeNameRelease(AttributeNamePress press) {
        this.press = press;
    }

    @Override
    public void run(CapturingContext context) {
        if (press.active) {
            var assembler = context.popAssembler();

            name = assembler.popString();

            assembler.expectEmpty();

            press.active = false;
        }
    }

    @Override
    public String getDescription() {
        return "RELEASE ATTRIBUTE NAME";
    }
}
