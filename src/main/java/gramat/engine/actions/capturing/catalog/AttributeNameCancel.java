package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class AttributeNameCancel extends CapturingAction {

    private final AttributeNamePress press;

    public AttributeNameCancel(AttributeNamePress press) {
        this.press = press;
    }

    @Override
    public void run(CapturingContext context) {
        if (press.active) {
            context.popAssembler();

            press.active = false;
        }
    }

    @Override
    public String getDescription() {
        return "CANCEL ATTRIBUTE NAME";
    }
}
