package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.CapturingSubAction;

abstract public class AbstractValueAccept extends CapturingSubAction<AbstractValuePress> {

    private final int beginPosition;

    public AbstractValueAccept(AbstractValuePress origin, int beginPosition) {
        super(origin);
        this.beginPosition = beginPosition;
    }

    @Override
    public final void run(CapturingContext context) {
        var endPosition = context.input.getPosition();
        var value = context.input.extract(beginPosition, endPosition);
        var parser = origin.getParser();

        context.peekAssembler().pushValue(value, parser);
    }

}
