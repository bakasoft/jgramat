package gramat.eval;

import gramat.eval.transactions.TransactionManager;
import gramat.framework.Context;
import gramat.input.Tape;

import java.util.List;
import java.util.Map;
import java.util.Stack;

public class EvalContext {

    public final Context ctx;
    public final Tape tape;
    public final Heap heap;

    private final Stack<Container> containers;

    private Integer position;

    public final TransactionManager manager;

    public EvalContext(Context ctx, Tape tape, Heap heap) {
        this.ctx = ctx;
        this.tape = tape;
        this.heap = heap;
        this.containers = new Stack<>();
        this.manager = new TransactionManager(this);
    }

    public void pushContainer() {
        ctx.debug("push container");
        containers.push(new Container());
    }

    public void addValue(Object value) {
        ctx.debug("add value %s", value);
        if (containers.isEmpty()) {
            throw new RejectedException("empty container stack");
        }

        var container = containers.peek();

        container.addValue(value);
    }

    public void setName(String name) {
        ctx.debug("set name %s", name);
        if (containers.isEmpty()) {
            throw new RejectedException("empty container stack");
        }

        var container = containers.peek();

        container.addName(name);
    }

    public String getName() {
        if (containers.isEmpty()) {
            throw new RejectedException("empty container stack");
        }

        var container = containers.peek();
        return container.removeName();
    }

    public void setAttribute(String name, Object value) {
        ctx.debug("set attribute %s=%s", name, value);

        if (containers.isEmpty()) {
            throw new RejectedException("empty container stack");
        }

        var container = containers.peek();

        container.addAttribute(name, value);
    }

    public Map<String, ?> popAttributes() {
        ctx.debug("pop attributes");

        if (containers.isEmpty()) {
            throw new RejectedException("empty container stack");
        }

        var container = containers.pop();
        var attributes = container.removeAttributes();

        container.expectEmpty();

        return attributes;
    }

    public Object popValue() {
        ctx.debug("pop value");

        if (containers.isEmpty()) {
            throw new RejectedException("empty container stack");
        }

        var container = containers.pop();
        var values = container.removeValues();

        container.expectEmpty();

        if (values.isEmpty()) {
            return null;  // TODO should this be validated to only return null once?
        }
        else if (values.size() != 1) {
            throw new RejectedException("expected one value");
        }

        return values.get(0);
    }

    public List<?> popArray() {
        ctx.debug("pop array");

        if (containers.isEmpty()) {
            throw new RejectedException("empty container stack");
        }

        var container = containers.pop();
        var value = container.removeValues();

        container.expectEmpty();

        return value;
    }

    public void pushPosition(int position) {
        ctx.debug("push position: " + position);
        if (this.position != null) {
            throw new RejectedException("position already started");
        }
        this.position = position;
    }

    public int popPosition() {
        ctx.debug("pop position: " + position);
        if (this.position == null) {
            throw new RejectedException("position is not set");
        }
        var aux = this.position;
        this.position = null;
        return aux;
    }
}
