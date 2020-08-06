package gramat.engine.actions.capturing;

import gramat.engine.parsers.ValueParser;
import gramat.expressions.capturing.ValueRuntime;

public class ValueRelease extends ValueAction {

    private final ValueParser parser;
    private final ValuePress press;

    public ValueRelease(ValueParser parser, ValuePress press) {
        this.parser = parser;
        this.press = press;
    }

    @Override
    public void run(ValueRuntime runtime) {
        if (press.position != null) {
            var position0 = press.position;
            var positionF = runtime.input.getPosition();
            var value = runtime.input.extract(position0, positionF);

            runtime.peekAssembler().pushValue(value, parser);

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
