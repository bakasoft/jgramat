package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class ValuePress extends CapturingAction {

    @Override
    public void run(CapturingContext context) {
        int beginPosition = context.input.getPosition();

        context.future.enqueue(new ValueReject(this, beginPosition));
    }

    @Override
    public String getDescription() {
        return "PRESS VALUE";
    }
}
