package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class ObjectPress extends CapturingAction {

    // TODO add comment why this is active by default
    public boolean active = true;

    @Override
    public void run(CapturingContext context) {
        if (!active) {
            context.pushAssembler();
            active = true;
        }
    }

    @Override
    public String getDescription() {
        return "PRESS OBJECT";
    }

}
