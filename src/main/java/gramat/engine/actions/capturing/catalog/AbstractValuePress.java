package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.parsers.ValueParser;

abstract public class AbstractValuePress  extends CapturingAction {

    abstract protected Class<? extends AbstractValueAccept> getAcceptType();

    abstract protected Class<? extends AbstractValueReject> getRejectType();

    abstract protected AbstractValueReject createReject(int beginPosition);

    abstract protected AbstractValueAccept createAccept(int beginPosition);

    public abstract ValueParser getParser();

    @Override
    public void run(CapturingContext context) {
        int beginPosition = context.input.getPosition();

        context.future.append(createReject(beginPosition));
    }
}