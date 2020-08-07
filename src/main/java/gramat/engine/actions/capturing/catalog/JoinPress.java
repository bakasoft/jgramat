package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class JoinPress extends CapturingAction {

    @Override
    public void run(CapturingContext context) {
        // TODO implement
    }

    @Override
    public String getDescription() {
        return "PRESS JOIN";
    }

    @Override
    public final int getOrder() {
        return 1;
    }
}
