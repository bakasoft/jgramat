package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class AttributeValueSustain extends CapturingAction {

    private final AttributeValuePress press;

    public AttributeValueSustain(AttributeValuePress press) {
        this.press = press;
    }

    @Override
    public void run(CapturingContext context) {
        // TODO
    }

    @Override
    public String getDescription() {
        return "SUSTAIN ATTRIBUTE VALUE";
    }
}
