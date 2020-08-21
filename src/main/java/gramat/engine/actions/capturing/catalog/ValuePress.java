package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.marks.MarkFactory;
import gramat.engine.parsers.ValueParser;

public class ValuePress extends CapturingAction {

    private final ValueParser parser;

    public ValuePress(ValueParser parser) {
        this.parser = parser;
    }

    @Override
    public void run(CapturingContext context) {
        var beginPosition = context.input.getPosition();
        var mark = MarkFactory.createMark(beginPosition);

        context.pushMark(this, mark);
    }

    protected ValueParser getParser() {
        return parser;
    }

    @Override
    public String getDescription() {
        return "PRESS VALUE";
    }
}
