package gramat.actions;

import gramat.Context;
import gramat.Debug;

import java.util.LinkedHashMap;
import java.util.Map;

abstract public class Action {

    private boolean disposed;

    abstract public boolean stack(Action other);

    abstract protected void fillAttributes(Map<String, String> attributes);

    abstract protected void run_impl(Context context);

    abstract protected void dispose_impl();

    final public void run(Context context) {
        Debug.log("RUN: " + getClass().getSimpleName());
        run_impl(context);
    }

    final public Map<String, String> getAttributes() {
        var attributes = new LinkedHashMap<String, String>();

        fillAttributes(attributes);

        return attributes;
    }

    final public boolean isDisposed() {
        return disposed;
    }

    final public void dispose() {
        dispose_impl();
        disposed = true;
    }
}
