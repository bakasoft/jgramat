package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.CapturingSubAction;

abstract public class AbstractContainerReject extends CapturingSubAction<AbstractContainerPress> {

    public AbstractContainerReject(AbstractContainerPress origin) {
        super(origin);
    }

    @Override
    public void run(CapturingContext context) {
        // discard content
        context.popAssembler();
    }

}
