package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.CapturingSubAction;

public class ValueAccept extends CapturingSubAction<ValuePress> {

    private final int beginPosition;

    public ValueAccept(ValuePress origin, int beginPosition) {
        super(origin);
        this.beginPosition = beginPosition;
    }

    @Override
    public void run(CapturingContext context) {
        var endPosition = context.input.getPosition();
        var value = context.input.extract(beginPosition, endPosition);
        var parser = origin.getParser();

        context.peekAssembler().pushValue(value, parser);
    }

    @Override
    public String getDescription() {
        return "VALUE ACCEPT";
    }
}
