package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class ObjectPress extends CapturingAction {

    @Override
    public void run(CapturingContext context) {
        context.pushAssembler();

        context.future.append(new ObjectReject(this));
    }

    @Override
    public String getDescription() {
        return "PRESS OBJECT";
    }

}
