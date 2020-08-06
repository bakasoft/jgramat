package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class ValuePress extends CapturingAction {

    public Integer position;

    @Override
    public void run(CapturingContext context) {
        if (position != null) {
            System.out.println("WARNING: re-starting @ " + this);
        }

        position = context.input.getPosition();
    }

    @Override
    public String getDescription() {
        return "PRESS VALUE";
    }
}
