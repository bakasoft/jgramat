package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class AttributeStaticReject extends CapturingAction {

    private final String name;
    private final AttributeStaticPress press;

    public AttributeStaticReject(String name, AttributeStaticPress press) {
        this.name = name;
        this.press = press;
    }

    @Override
    public void run(CapturingContext context) {
        context.popAssembler();
    }

    @Override
    public String getDescription() {
        return "REJECT ATTRIBUTE: " + name;
    }
}
