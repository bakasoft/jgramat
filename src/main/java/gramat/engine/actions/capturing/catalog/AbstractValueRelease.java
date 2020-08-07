package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.CapturingSubAction;

abstract public class AbstractValueRelease extends CapturingSubAction<AbstractValuePress> {

    public AbstractValueRelease(AbstractValuePress origin) {
        super(origin);
    }

    @Override
    public void run(CapturingContext context) {
        var accept = context.tryPostpone(origin.getAcceptType());

        if (accept == null) {
            var reject  = context.present.removeLast(origin.getRejectType());

            if (reject == null) {
                throw new RuntimeException("expected reject");
            }

            context.future.append(origin.createAccept(reject.beginPosition));
        }
    }
}
