package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.CapturingSubAction;

abstract public class AbstractContainerSustain extends CapturingSubAction<AbstractContainerPress> {

    public AbstractContainerSustain(AbstractContainerPress origin) {
        super(origin);
    }

    @Override
    public final void run(CapturingContext context) {
        if (context.tryPostpone(origin.getRejectClass()) == null) {
            throw new RuntimeException("expected reject to be postponed");
        }
    }

}
