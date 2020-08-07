package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.CapturingSubAction;

abstract public class AbstractValueReject extends CapturingSubAction<AbstractValuePress> {

    public final int beginPosition;

    public AbstractValueReject(AbstractValuePress origin, int beginPosition) {
        super(origin);
        this.beginPosition = beginPosition;
    }

    @Override
    public final void run(CapturingContext context) {
        // just don't capture the value
    }

}
