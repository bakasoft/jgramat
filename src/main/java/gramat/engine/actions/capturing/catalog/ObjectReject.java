package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class ObjectReject extends CapturingAction {

    private final ObjectPress press;

    public ObjectReject(ObjectPress press) {
        this.press = press;
    }

    @Override
    public void run(CapturingContext context) {
        context.popAssembler();
    }

    @Override
    public String getDescription() {
        return "CANCEL OBJECT";
    }
}
