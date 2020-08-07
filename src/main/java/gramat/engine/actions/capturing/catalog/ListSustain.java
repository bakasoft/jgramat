package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class ListSustain extends CapturingAction {

    private final ListPress press;

    public ListSustain(ListPress press) {
        this.press = press;
    }

    @Override
    public void run(CapturingContext context) {
        // TODO implement
    }

    @Override
    public String getDescription() {
        return "SUSTAIN LIST";
    }

    @Override
    public final int getOrder() {
        return 2;
    }
}
