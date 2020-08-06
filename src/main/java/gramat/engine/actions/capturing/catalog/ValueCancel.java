package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class ValueCancel extends CapturingAction {

    private final ValuePress press;

    public ValueCancel(ValuePress press) {
        this.press = press;
    }

    @Override
    public void run(CapturingContext context) {
        if (press.position == null) {
            System.out.println("WARNING: canceling before start @ " + this);
        }

        press.position = null;
    }

    @Override
    public String getDescription() {
        return "CANCEL VALUE";
    }
}
