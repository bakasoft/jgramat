package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.parsers.ValueParser;

abstract public class AbstractValuePress  extends CapturingAction {

    abstract protected Class<? extends AbstractValueAccept> getAcceptType();

    abstract protected Class<? extends AbstractValueReject> getRejectType();

    abstract protected AbstractValueReject createReject(int beginPosition);

    abstract protected AbstractValueAccept createAccept(int beginPosition);

    abstract protected ValueParser getParser();

    @Override
    public final void run(CapturingContext context) {
        int beginPosition = context.input.getPosition();

        context.enqueue(createReject(beginPosition));
    }
}