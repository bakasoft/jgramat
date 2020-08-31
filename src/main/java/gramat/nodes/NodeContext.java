package gramat.nodes;

import java.util.Stack;
import java.util.function.Supplier;

public class NodeContext {
    private final Stack<Node> controlStack;

    public NodeContext() {
        controlStack = new Stack<>();
    }

    public Node enter(Node node, Supplier<Node> compiler) {
        if (controlStack.contains(node)) {
            return node;
        }

        controlStack.push(node);

        var result = compiler.get();

        controlStack.pop();

        return result;
    }
}
