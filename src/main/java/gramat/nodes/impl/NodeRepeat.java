package gramat.nodes.impl;

import gramat.Context;
import gramat.nodes.Node;
import gramat.nodes.NodeDecorator;

public class NodeRepeat extends NodeDecorator {

    public NodeRepeat(Node node) {
        super(node);
    }

    @Override
    public void eval(Context context) {
        while (content.test(context.tape.peek())) {
            content.eval(context);
        }
    }

    @Override
    public boolean test(char value) {
        return content.test(value);
    }

    @Override
    public boolean isOptional() {
        return true;
    }

}
