package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.parsers.ValueParser;

public class ValueAccept extends CapturingAction {

    private final ValuePress press;
    private final ValueParser parser;

    private final int beginPosition;

    public ValueAccept(ValuePress press, ValueParser parser, int beginPosition) {
        this.press = press;
        this.parser = parser;
        this.beginPosition = beginPosition;
    }

    @Override
    public void run(CapturingContext context) {
        var endPosition = context.input.getPosition();
        var value = context.input.extract(beginPosition, endPosition);

        context.peekAssembler().pushValue(value, parser);
    }

    @Override
    public String getDescription() {
        return "VALUE ACCEPT";
    }
}
