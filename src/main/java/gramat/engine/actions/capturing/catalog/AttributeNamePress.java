package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class AttributeNamePress extends CapturingAction {

    public boolean active;

    @Override
    public void run(CapturingContext context) {
        if (!active) {
            context.pushAssembler();
            active = true;
        }
    }

    @Override
    public String getDescription() {
        return "PRESS ATTRIBUTE NAME";
    }

}
