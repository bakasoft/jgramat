package gramat.nodes.impl;

import gramat.Context;
import gramat.nodes.Node;
import gramat.nodes.NodeContext;
import gramat.nodes.NodeVertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeSequence implements Node {

    private List<Node> nodes;

    public NodeSequence() {
        this.nodes = new ArrayList<>();
    }

    public NodeSequence(List<Node> nodes) {
        this.nodes = new ArrayList<>(nodes);
    }

    public void join(NodeSequence other) {
        this.nodes.addAll(other.nodes);
    }

    public void append(Node node) {
        nodes.add(node);
    }

    @Override
    public void eval(Context context) {
        for (var node : nodes) {
            node.eval(context);
        }
    }

    @Override
    public boolean test(char value) {
        for (var node : nodes) {
            if (node.test(value)) {
                return true;
            }
            else if (!node.isOptional()) {
                break;
            }
        }

        return false;
    }

    @Override
    public Node collapse(NodeContext context) {
        return context.enter(this, () -> {
            var newNodes = new ArrayList<Node>();

            for (var node : nodes) {
                var collapsedNode = node.collapse(context);

                if (collapsedNode instanceof NodeSequence) {
                    var sequence = (NodeSequence)collapsedNode;

                    newNodes.addAll(sequence.nodes);
                }
                else if (collapsedNode != null) {
                    newNodes.add(collapsedNode);
                }
            }

            if (newNodes.isEmpty()) {
                return null;
            }
            else if (newNodes.size() == 1) {
                return newNodes.get(0);
            }

            nodes = newNodes;
            return this;
        });
    }

    @Override
    public Node compile(NodeContext context) {
        return context.enter(this, () -> {
            for (var i = 0; i < nodes.size(); i++) {
                var compiledNode = nodes.get(i).compile(context);

                nodes.set(i, compiledNode);
            }
            return this;
        });
    }

    @Override
    public Node tryStack(Node other) {
        if (other instanceof NodeSequence) {
            var otherSequence = (NodeSequence)other;
            var stackedNodes = Node.tryStackNodes(this.nodes, otherSequence.nodes);

            if (stackedNodes != null) {
                this.nodes = stackedNodes;
                return this;
            }
        }
        return null;
    }

    @Override
    public List<NodeVertex> toVertices() {
        var root = NodeVertex.fromSequence(nodes);

        return List.of(root);
    }

    @Override
    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    @Override
    public boolean isOptional() {
        for (var node : nodes) {
            if (!node.isOptional()) {
                return false;
            }
        }
        return true;
    }
}
