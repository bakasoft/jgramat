package gramat.nodes.impl;

import gramat.Context;
import gramat.nodes.Node;
import gramat.nodes.NodeDecorator;

public class NodeOptional extends NodeDecorator {

    public NodeOptional(Node node) {
        super(node);
    }

    @Override
    protected void eval_impl(Context context) {
        if (content.test(context.tape.peek())) {
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

    @Override
    public Node stack(Node other) {
        if (other instanceof NodeOptional) {
            var otherDecorator = (NodeOptional)other;
            var newContent = this.content.stack(otherDecorator.content);
            var result = new NodeOptional(newContent);
            result.addActionsFrom(this);
            result.addActionsFrom(other);
            return result;
        }
        return null;
    }

    @Override
    public Node shallowCopy() {
        var copy = new NodeOptional(content);
        copy.addActionsFrom(this);
        return copy;
    }
}
