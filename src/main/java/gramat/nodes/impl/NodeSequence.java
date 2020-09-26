package gramat.nodes.impl;

import gramat.Context;
import gramat.nodes.Node;
import gramat.nodes.NodeContext;
import gramat.nodes.NodeVertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeSequence extends Node {

    private List<Node> nodes;

    public NodeSequence() {
        this.nodes = new ArrayList<>();
    }

    public NodeSequence(List<Node> nodes) {
        this.nodes = new ArrayList<>(nodes);
    }

    @Override
    protected void eval_impl(Context context) {
        for (var node : nodes) {
            node.eval(context);
        }
    }

    public void join(NodeSequence other) {
        this.nodes.addAll(other.nodes);
    }

    public void append(Node node) {
        nodes.add(node);
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

                    sequence.moveActionsDown();

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
    public Node stack(Node other) {
        if (other instanceof NodeSequence) {
            var otherSequence = (NodeSequence)other;
            var stackedNodes = Node.tryStackNodes(this.nodes, otherSequence.nodes);

            if (stackedNodes != null) {
                var result = new NodeSequence(stackedNodes);
                result.addActionsFrom(this);
                result.addActionsFrom(other);
                return result;
            }
        }
        return null;
    }

    public void moveActionsDown() {
        if (nodes.isEmpty()) {
            throw new RuntimeException();
        }
        else if (nodes.size() == 1) {
            var newNodes = new ArrayList<Node>();
            var newNode = nodes.get(0).shallowCopy();

            newNodes.add(newNode);

            newNode.addPreActionsFrom(this);
            newNode.addPostActionsFrom(this);

            this.clearActions();

            this.nodes = newNodes;
        }
        else {
            var newFirst = nodes.get(0).shallowCopy();
            var newLast = nodes.get(nodes.size() - 1).shallowCopy();

            nodes.set(0, newFirst);
            nodes.set(nodes.size() - 1, newLast);

            newFirst.addPreActionsFrom(this);
            newLast.addPostActionsFrom(this);

            this.clearActions();
        }
    }

    @Override
    public List<NodeVertex> toVertices() {
        moveActionsDown();

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

    @Override
    public Node shallowCopy() {
        var copy = new NodeSequence(nodes);
        copy.addActionsFrom(this);
        return copy;
    }
}
