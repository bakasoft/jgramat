package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class ValueReject extends CapturingAction {

    private final ValuePress press;
    public final int beginPosition;

    public ValueReject(ValuePress press, int beginPosition) {
        this.press = press;
        this.beginPosition = beginPosition;
    }

    @Override
    public void run(CapturingContext context) {
        // just don't capture the value
    }

    @Override
    public String getDescription() {
        return "VALUE REJECT";
    }
}
