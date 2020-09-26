package gramat.actions.impl;

import gramat.Context;
import gramat.actions.Action;

import java.util.Map;

public class ValueBegin extends Action {

    @Override
    protected void run_impl(Context context) {
        context.pushBegin();
    }

    @Override
    public boolean stack(Action other) {
        if (other instanceof ValueBegin) {
            other.dispose();
            return true;
        }
        return false;
    }

    @Override
    protected void fillAttributes(Map<String, String> attributes) {

    }

    @Override
    protected void dispose_impl() {
        // nothing to dispose
    }

}
