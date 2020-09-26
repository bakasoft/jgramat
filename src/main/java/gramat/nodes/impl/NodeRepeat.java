package gramat.nodes.impl;

import gramat.Context;
import gramat.nodes.Node;
import gramat.nodes.NodeDecorator;

public class NodeRepeat extends NodeDecorator {

    public NodeRepeat(Node node) {
        super(node);
    }

    @Override
    protected void eval_impl(Context context) {
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

    @Override
    public Node stack(Node other) {
        if (other instanceof NodeRepeat) {
            var otherDecorator = (NodeRepeat)other;
            var newContent = this.content.stack(otherDecorator.content);
            var result = new NodeRepeat(newContent);
            result.addActionsFrom(this);
            result.addActionsFrom(other);
            return result;
        }
        return null;
    }

    @Override
    public Node shallowCopy() {
        var copy = new NodeRepeat(content);
        copy.addActionsFrom(this);
        return copy;
    }

}
