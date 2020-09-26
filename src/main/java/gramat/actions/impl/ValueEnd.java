package gramat.actions.impl;

import gramat.Context;
import gramat.actions.Action;
import gramat.parsers.ValueParser;

import java.util.Map;

public class ValueEnd extends Action {

    private final ValueParser parser;

    public ValueEnd(ValueParser parser) {
        this.parser = parser;
    }

    @Override
    protected void run_impl(Context context) {
        var begin = context.popBegin();
        var end = context.tape.getPosition();
        var text = context.tape.extract(begin, end);

        context.peekAssembler().pushValue(text, parser);
    }

    @Override
    public boolean stack(Action other) {
        if (other instanceof ValueEnd) {
            var endValue = (ValueEnd)other;

            if (endValue.parser != this.parser) {
                throw new RuntimeException("parser conflict");
            }
        }
        return false;
    }

    @Override
    protected void fillAttributes(Map<String, String> attributes) {

    }

    @Override
    protected void dispose_impl() {
        // nothing to dispose;
    }
}
