package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class ValueSustain extends CapturingAction {

    private final ValuePress press;

    public ValueSustain(ValuePress press) {
        this.press = press;
    }

    @Override
    public void run(CapturingContext context) {
        // TODO
    }

    @Override
    public String getDescription() {
        return "SUSTAIN VALUE";
    }
}
