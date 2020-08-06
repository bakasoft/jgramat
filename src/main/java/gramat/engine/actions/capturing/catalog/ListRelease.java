package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class ListRelease extends CapturingAction {

    private final ListPress press;

    public ListRelease(ListPress press) {
        this.press = press;
    }

    @Override
    public void run(CapturingContext context) {
        // TODO implement
    }

    @Override
    public String getDescription() {
        return "RELEASE LIST";
    }
}
