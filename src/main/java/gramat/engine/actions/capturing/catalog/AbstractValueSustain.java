package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.CapturingSubAction;

abstract public class AbstractValueSustain extends CapturingSubAction<AbstractValuePress> {

    public AbstractValueSustain(AbstractValuePress origin) {
        super(origin);
    }

    @Override
    public void run(CapturingContext context) {
        var reject = context.tryPostpone(origin.getRejectType());

        if (reject == null) {
            throw new RuntimeException("expected reject to be posposed");
        }
    }

}
