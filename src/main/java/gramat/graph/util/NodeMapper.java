package gramat.graph.util;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.graph.Root;
import gramat.util.Chain;

import java.util.ArrayList;
import java.util.HashMap;

public class NodeMapper {

    private final HashMap<Node, Node> nodes;
    private final Graph newGraph;
    private final NodeMapper parent;

    public NodeMapper(Graph newGraph) {
        this(newGraph, null);
    }

    public NodeMapper(NodeMapper parent) {
        this(parent.newGraph, parent);
    }

    private NodeMapper(Graph newGraph, NodeMapper parent) {
        this.newGraph = newGraph;
        this.parent = parent;
        this.nodes = new HashMap<>();
    }

    public void set(Node oldNode, Node newNode) {
        var currentNode = nodes.get(oldNode);

        if (currentNode != null && currentNode != newNode) {
            throw new RuntimeException(String.format("double mapping! %s -> %s | %s", oldNode.id, currentNode.id, newNode.id));
        }

        nodes.put(oldNode, newNode);
    }

    public void set(Chain<Node> oldNodes, Node newNode) {
        for (var oldNode : oldNodes) {
            set(oldNode, newNode);
        }
    }

    public Node make(Node oldNode) {
        return nodes.computeIfAbsent(oldNode, newGraph::createNodeFrom);
    }

    public Node find(Node oldNode) {
        var newNode = nodes.get(oldNode);

        if (newNode == null && parent != null) {
            newNode = parent.find(oldNode);
        }

        if (newNode == null) {
            throw new RuntimeException();
        }

        return newNode;
    }

    private Chain<Node> find(Chain<Node> oldNodes) {
        var newNodes = new ArrayList<Node>(oldNodes.size());

        for (var oldNode : oldNodes) {
            var newNode = find(oldNode);

            newNodes.add(newNode);
        }

        return Chain.of(newNodes);
    }

    public Root find(Root oldRoot) {
        var newSource = find(oldRoot.source);
        var newTargets = find(oldRoot.targets);
        return new Root(newSource, newTargets);
    }
}
