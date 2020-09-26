package gramat.nodes.impl;

import gramat.Context;
import gramat.nodes.Node;
import gramat.nodes.NodeContext;
import gramat.nodes.NodeVertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeAlternation extends Node {

    private List<Node> nodes;

    public NodeAlternation() {
        this.nodes = new ArrayList<>();
    }

    public NodeAlternation(List<Node> nodes) {
        this.nodes = new ArrayList<>(nodes);
    }

    @Override
    protected void eval_impl(Context context) {
        var chr = context.tape.peek();

        for (var node : nodes) {
            if (node.test(chr)) {
                node.eval(context);
                break;
            }
        }
    }

    public void join(NodeAlternation alt) {
        nodes.addAll(alt.nodes);
    }

    public void add(Node node) {
        nodes.add(node);
    }

    @Override
    public boolean test(char value) {
        for (var node : nodes) {
            if (node.test(value)) {
                return true;
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

                if (collapsedNode instanceof NodeAlternation) {
                    var alternation = (NodeAlternation) collapsedNode;

                    alternation.moveActionsDown();

                    newNodes.addAll(alternation.nodes);
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
            // compile all nodes
            for (var i = 0; i < nodes.size(); i++) {
                var compiledNode = nodes.get(i).compile(context);

                nodes.set(i, compiledNode);
            }

            // stack vertices and recompile to nodes
            var vertices = toVertices();

            vertices = NodeVertex.stackVertices(vertices);

            return NodeVertex.toNode(vertices);
        });
    }

    @Override
    public Node stack(Node other) {
        if (other instanceof NodeAlternation) {
            var otherAlternation = (NodeAlternation)other;

            // TODO should this be a different algorithm than sequence? 🤔
            var stackedNodes = Node.tryStackNodes(this.nodes, otherAlternation.nodes);

            if (stackedNodes != null) {
                var result = new NodeAlternation(stackedNodes);
                result.addActionsFrom(this);
                result.addActionsFrom(other);
                return result;
            }
        }
        return null;
    }

    public void moveActionsDown() {
        var newNodes = new ArrayList<Node>();

        for (var node : nodes) {
            var newNode = node.shallowCopy();

            newNode.addActionsFrom(this);

            newNodes.add(newNode);
        }

        this.nodes = newNodes;
        this.clearActions();
    }

    @Override
    public List<NodeVertex> toVertices() {
        var vertices = new ArrayList<NodeVertex>();

        moveActionsDown();

        for (var node : nodes) {
            vertices.addAll(node.toVertices());
        }

        return vertices;
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
        var copy = new NodeAlternation(nodes);
        copy.addActionsFrom(this);
        return copy;
    }
}
