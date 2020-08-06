package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class AttributeCancel extends CapturingAction {

    private final String name;
    private final AttributePress press;

    public AttributeCancel(String name, AttributePress press) {
        this.name = name;
        this.press = press;
    }

    @Override
    public void run(CapturingContext context) {
        context.popAssembler();
    }

    @Override
    public String getDescription() {
        return "CANCEL ATTRIBUTE: " + name;
    }
}
