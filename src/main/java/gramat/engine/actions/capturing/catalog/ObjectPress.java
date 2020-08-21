package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class ObjectPress extends CapturingAction {

    @Override
    public final void run(CapturingContext context) {
        context.pushAssembler();
    }

    @Override
    public String getDescription() {
        return "PRESS OBJECT";
    }

}
