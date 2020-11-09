package gramat.eval;

import gramat.scheme.core.badges.Badge;
import gramat.util.StringUtils;

import java.util.Stack;

public class Heap {

    private final Badge empty;
    private final Stack<Badge> stack;

    public Heap(Badge empty) {
        this.empty = empty;
        this.stack = new Stack<>();
    }

    public Badge peek() {
        if (stack.isEmpty()) {
            return empty;
        }
        return stack.peek();
    }

    public boolean push(Badge badge) {
        stack.push(badge);
        return true;
    }

    public boolean pop(Badge badge) {
        var peek = stack.pop();

        if (peek != badge) {
            throw new RuntimeException("expected " + badge + " but got " + peek);
        }

        return true;
    }

    public boolean notEmpty() {
        return stack.size() > 0;
    }

    @Override
    public String toString() {
        return "[" + StringUtils.join(", ", stack) + "]";
    }
}
