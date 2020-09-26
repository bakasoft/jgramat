package gramat.actions.impl;

import gramat.Context;
import gramat.actions.Action;

import java.util.ArrayList;
import java.util.Map;

public class ArrayEnd extends Action {

    private Object typeHint;

    public ArrayEnd(Object typeHint) {
        this.typeHint = typeHint;
    }

    @Override
    protected void run_impl(Context context) {
        var assembler = context.popAssembler();
        var values = new ArrayList<>();

        while (!assembler.isEmpty()) {
            values.add(assembler.popValue());
        }

        assembler.expectEmpty();

        // TODO use type hint

        context.peekAssembler().pushValue(values);
    }

    @Override
    public boolean stack(Action other) {
        if (other instanceof ArrayEnd) {
            var begin = (ArrayEnd)other;

            return begin.typeHint == typeHint;
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
