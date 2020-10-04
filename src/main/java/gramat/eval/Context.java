package gramat.eval;

import gramat.framework.Logger;
import gramat.input.Tape;

import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Context {

    public final Tape tape;
    public final Logger logger;

    private final Stack<Container> containers;

    private Integer position;

    public Context(Logger logger, Tape tape) {
        this.logger = logger;
        this.tape = tape;

        containers = new Stack<>();
    }

    public void pushContainer() {
        logger.debug("push container");
        containers.push(new Container());
    }

    public void addValue(Object value) {
        logger.debug("add value %s", value);
        if (containers.isEmpty()) {
            throw new RejectedException("empty container stack");
        }

        var container = containers.peek();

        container.addValue(value);
    }

    public void setName(String name) {
        logger.debug("set name %s", name);
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
        logger.debug("set attribute %s=%s", name, value);

        if (containers.isEmpty()) {
            throw new RejectedException("empty container stack");
        }

        var container = containers.peek();

        container.addAttribute(name, value);
    }

    public Map<String, ?> popAttributes() {
        logger.debug("pop attributes");

        if (containers.isEmpty()) {
            throw new RejectedException("empty container stack");
        }

        var container = containers.pop();
        var attributes = container.removeAttributes();

        container.expectEmpty();

        return attributes;
    }

    public Object popValue() {
        logger.debug("pop value");

        if (containers.isEmpty()) {
            throw new RejectedException("empty container stack");
        }

        var container = containers.pop();
        var values = container.removeValues();

        container.expectEmpty();

        if (values.size() != 1) {
            throw new RejectedException("expected one value");
        }

        return values.get(0);
    }

    public List<?> popArray() {
        logger.debug("pop array");

        if (containers.isEmpty()) {
            throw new RejectedException("empty container stack");
        }

        var container = containers.pop();
        var value = container.removeValues();

        container.expectEmpty();

        return value;
    }

    public void pushPosition(int position) {
        logger.debug("push position: " + position);
        if (this.position != null) {
            throw new RejectedException("position already started");
        }
        this.position = position;
    }

    public int popPosition() {
        logger.debug("pop position: " + position);
        if (this.position == null) {
            throw new RejectedException("position is not set");
        }
        var aux = this.position;
        this.position = null;
        return aux;
    }
}
