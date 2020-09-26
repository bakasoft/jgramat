package gramat.actions.impl;

import gramat.Context;
import gramat.actions.Action;

import java.util.Map;
import java.util.Objects;

public class NameEnd extends Action {

    @Override
    protected void run_impl(Context context) {
        var assembler = context.popAssembler();
        var name = assembler.popString();

        assembler.expectEmptyValues();

        context.peekAssembler().pushName(name);
    }

    @Override
    public boolean stack(Action other) {
        return other instanceof NameEnd;
    }

    @Override
    protected void fillAttributes(Map<String, String> attributes) {

    }

    @Override
    protected void dispose_impl() {

    }
}
