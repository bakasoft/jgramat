package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

abstract public class AbstractContainerPress extends CapturingAction {

    abstract protected Object createInitializator(CapturingContext context);
    abstract protected AbstractContainerReject createReject(Object initialization);
    abstract protected AbstractContainerAccept createAccept(AbstractContainerReject reject);

    abstract protected Class<? extends AbstractContainerReject> getRejectClass();
    abstract protected Class<? extends AbstractContainerAccept> getAcceptClass();

    @Override
    public final void run(CapturingContext context) {
        var initializator = createInitializator(context);

        context.pushAssembler();

        context.enqueue(createReject(initializator));
    }
}
