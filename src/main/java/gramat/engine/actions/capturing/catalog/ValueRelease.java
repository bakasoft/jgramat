package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.parsers.ValueParser;
import gramat.engine.actions.capturing.CapturingContext;

public class ValueRelease extends CapturingAction {

    private final ValueParser parser;
    private final ValuePress press;

    public ValueRelease(ValueParser parser, ValuePress press) {
        this.parser = parser;
        this.press = press;
    }

    @Override
    public void run(CapturingContext context) {
        if (press.position != null) {
            var position0 = press.position;
            var positionF = context.input.getPosition();
            var value = context.input.extract(position0, positionF);

            context.peekAssembler().pushValue(value, parser);

            press.position = null;
        } else {
            System.out.println("Missing start point");
        }
    }

    @Override
    public String getDescription() {
        return "RELEASE VALUE";
    }
}
