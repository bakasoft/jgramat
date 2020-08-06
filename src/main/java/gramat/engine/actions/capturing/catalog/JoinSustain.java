package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class JoinSustain extends CapturingAction {

    private final JoinPress press;

    public JoinSustain(JoinPress press) {
        this.press = press;
    }

    @Override
    public void run(CapturingContext context) {
        // TODO implement
    }

    @Override
    public String getDescription() {
        return "SUSTAIN JOIN";
    }
}
