package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.parsers.ValueParser;

public class ValueAccept extends CapturingAction {

    private final ValuePress press;
    private final ValueParser parser;

    private final int beginPosition;

    public int endPosition;

    public ValueAccept(ValuePress press, ValueParser parser, int beginPosition, int endPosition) {
        this.press = press;
        this.parser = parser;
        this.beginPosition = beginPosition;
        this.endPosition = endPosition;
    }

    @Override
    public void run(CapturingContext context) {
        endPosition = context.input.getPosition();  // TODO check if this replace the field

        var value = context.input.extract(beginPosition, endPosition);

        context.peekAssembler().pushValue(value, parser);
    }

    @Override
    public String getDescription() {
        return "VALUE ACCEPT";
    }
}
