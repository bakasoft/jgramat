package gramat.actions.impl;

import gramat.Context;
import gramat.actions.Action;

import java.util.Map;

public class ObjectEnd extends Action {

    private final Object defaultType;

    public ObjectEnd(Object defaultType) {
        this.defaultType = defaultType;
    }

    @Override
    protected void run_impl(Context context) {
        var assembler = context.popAssembler();
        var attributes = assembler.getAttributes();

        // TODO if default type is null, read it from name stack

        assembler.expectEmptyValues();

        // TODO use type

        context.peekAssembler().pushValue(attributes);
    }

    @Override
    public boolean stack(Action other) {
        if (other instanceof ObjectEnd) {
            var begin = (ObjectEnd)other;

            return begin.defaultType == defaultType;
        }
        return false;
    }

    @Override
    protected void fillAttributes(Map<String, String> attributes) {

    }

    @Override
    protected void dispose_impl() {

    }
}
