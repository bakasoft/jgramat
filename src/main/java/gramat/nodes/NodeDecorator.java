package gramat.nodes;

import java.util.List;
import java.util.Objects;

abstract public class NodeDecorator extends Node {

    protected Node content;

    public NodeDecorator(Node content) {
        this.content = Objects.requireNonNull(content);
    }

    @Override
    public final Node collapse(NodeContext context) {
        return context.enter(this, () -> {
            var newNode = content.collapse(context);

            if (newNode == null) {
                return null;
            }

            content = newNode;
            return this;
        });
    }

    @Override
    public final Node compile(NodeContext context) {
        return context.enter(this, () -> {
            content = content.compile(context);
            return this;
        });
    }

    @Override
    public final List<NodeVertex> toVertices() {
        return List.of(new NodeVertex(this));
    }

    public Node getContent() {
        return content;
    }

    @Override
    public List<Node> getNodes() {
        return List.of(content);
    }
}
