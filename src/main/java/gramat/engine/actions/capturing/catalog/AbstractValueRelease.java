package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.CapturingSubAction;

abstract public class AbstractValueRelease extends CapturingSubAction<AbstractValuePress> {

    public AbstractValueRelease(AbstractValuePress origin) {
        super(origin);
    }

    @Override
    public final void run(CapturingContext context) {
        var accept = context.tryPostpone(origin.getAcceptType());

        if (accept == null) {
            var reject  = context.dequeue(origin.getRejectType());

            if (reject != null) {
                context.enqueue(origin.createAccept(reject.beginPosition));

            }
            // TODO Confirm if is OK ignore missing reject
            // else {
            //     throw new RuntimeException("expected reject");
            // }
        }
    }
}
