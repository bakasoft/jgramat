package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class AttributeNameSustain extends CapturingAction {

    private final AttributeNamePress press;

    public AttributeNameSustain(AttributeNamePress press) {
        this.press = press;
    }

    @Override
    public void run(CapturingContext context) {
        // TODO
    }

    @Override
    public String getDescription() {
        return "SUSTAIN ATTRIBUTE NAME";
    }
}
