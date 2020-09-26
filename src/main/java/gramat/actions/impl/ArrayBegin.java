package gramat.actions.impl;

import gramat.Context;
import gramat.actions.Action;

import java.util.Map;

public class ArrayBegin extends Action {

    @Override
    protected void run_impl(Context context) {
        context.pushAssembler();
    }

    @Override
    public boolean stack(Action other) {
        return other instanceof ArrayBegin;
    }

    @Override
    protected void fillAttributes(Map<String, String> attributes) {

    }

    @Override
    protected void dispose_impl() {

    }

}