package org.bakasoft.gramat.capturing;

import java.util.Stack;
import java.util.function.Function;

public class TextBuilder {

    private final Stack<StringBuilder> stack;

    public TextBuilder() {
        stack = new Stack<>();

        stack.push(new StringBuilder());
    }

    public void append(char c) {
        stack.peek().append(c);
    }

    public void append(CharSequence chars) {
        stack.peek().append(chars);
    }

    public void beginTransaction() {
        stack.push(new StringBuilder());
    }

    public String commitTransaction() {
        return commitTransaction(null);
    }

    public String commitTransaction(Function<String, String> transformation) {
        StringBuilder builder = stack.pop();

        if (transformation == null) {
            stack.peek().append(builder);

            return builder.toString();
        }

        String value = transformation.apply(builder.toString());

        stack.peek().append(value);

        return value;
    }

    public void rollbackTransaction() {
        stack.pop();
    }
}
