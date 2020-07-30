package gramat.engine.stack;

import java.util.Objects;
import java.util.Stack;

public class ControlStack {

    private final Stack<String> stack;

    public ControlStack() {
        stack = new Stack<>();
    }

    public void push(String token) {
        stack.push(token);
    }

    public boolean test(String token) {
        if (stack.isEmpty()) {
            return false;
        }

        return Objects.equals(stack.peek(), token);
    }

    public boolean empty() {
        return stack.isEmpty();
    }

    public void pop() {
        stack.pop();
    }

    public boolean active() {
        return stack.size() > 0;
    }
}
