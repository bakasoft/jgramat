package gramat.nodes;

import gramat.nodes.impl.NodeAlternation;
import gramat.nodes.impl.NodeSequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeVertex {

    private Node node;
    private List<NodeVertex> edges;

    public NodeVertex(Node node) {
        this.node = node;
        this.edges = new ArrayList<>();
    }

    public boolean join(NodeVertex other) {
        var stacked = node.tryStack(other.node);

        if (stacked == null) {
            return false;
        }

        node = stacked;
        edges.addAll(other.edges);
        return true;
    }

    public NodeVertex fork(Node node) {
        var edge = new NodeVertex(node);

        edges.add(edge);

        return edge;
    }

    public Node getNode() {
        return node;
    }

    public List<NodeVertex> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public void stack() {
        edges = stackVertices(edges);
    }

    public static List<NodeVertex> stackVertices(List<NodeVertex> vertices) {
        var array = vertices.toArray(NodeVertex[]::new);
        var result = new ArrayList<NodeVertex>();

        for (var i = 0; i < array.length; i++) {
            if (array[i] != null) {
                var iEdge = array[i];

                for (var j = i + 1; j < array.length; j++) {
                    var jEdge = array[j];

                    if (iEdge.join(jEdge)) {
                        array[j] = null;
                    }
                }

                iEdge.stack();

                result.add(iEdge);
            }
        }

        return result;
    }

    public static Node toNode(List<NodeVertex> vertices) {
        if (vertices.isEmpty()) {
            throw new RuntimeException("no vertices provided");
        }
        else if (vertices.size() == 1) {
            return toNode(vertices.get(0));
        }

        var alternation = new NodeAlternation();

        for (var vertex : vertices) {
            var node = toNode(vertex);

            if (node instanceof NodeAlternation) {
                var subAlternation = (NodeAlternation) node;

                alternation.join(subAlternation);
            }
            else {
                alternation.add(node);
            }
        }

        return alternation;
    }

    private static Node toNode(NodeVertex vertex) {
        if (vertex.edges.isEmpty()) {
            return vertex.node;
        }

        var sequence = new NodeSequence();

        sequence.append(vertex.node);

        var next = toNode(vertex.edges);

        if (next instanceof NodeSequence) {
            var nextSequence = (NodeSequence)next;

            sequence.join(nextSequence);
        }
        else {
            sequence.append(next);
        }

        return sequence;
    }

    public static NodeVertex fromSequence(List<? extends Node> nodes) {
        var root = (NodeVertex)null;
        var last = (NodeVertex)null;

        for (var node : nodes) {
            if (root == null) {
                root = new NodeVertex(node);
                last = root;
            }
            else {
                last = last.fork(node);
            }
        }

        if (root == null) {
            throw new RuntimeException("no nodes provided");
        }

        return root;
    }

}
