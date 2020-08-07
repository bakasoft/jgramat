package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.CapturingSubAction;

abstract public class AbstractContainerRelease extends CapturingSubAction<AbstractContainerPress> {

    public AbstractContainerRelease(AbstractContainerPress origin) {
        super(origin);
    }

    @Override
    public final void run(CapturingContext context) {
        var accept = context.tryPostpone(origin.getAcceptClass());

        if (accept == null) {
            var reject = context.dequeue(origin.getRejectClass());

            if (reject != null) {
                context.enqueue(origin.createAccept(reject));
            }
            // TODO Confirm if is OK ignore missing reject
            // else {
            //    throw new RuntimeException("expected reject");
            // }
        }
    }

}
