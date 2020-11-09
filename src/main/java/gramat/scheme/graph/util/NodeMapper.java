package gramat.scheme.graph.util;

import gramat.scheme.core.actions.Event;
import gramat.scheme.graph.Graph;
import gramat.scheme.graph.Link;
import gramat.scheme.graph.Node;
import gramat.scheme.graph.Root;
import gramat.scheme.graph.sets.NodeSet;
import gramat.scheme.graph.sets.NodeSetMutable;

import java.util.HashMap;
import java.util.LinkedHashMap;

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
        this.nodes = new LinkedHashMap<>();
    }

    public void set(Node oldNode, Node newNode) {
        var currentNode = nodes.get(oldNode);

        if (currentNode != null && currentNode != newNode) {
            throw new RuntimeException(String.format("double mapping! %s -> %s | %s", oldNode.id, currentNode.id, newNode.id));
        }

        nodes.put(oldNode, newNode);
    }

    public Node copy(Node oldNode) {
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

    private NodeSet find(NodeSet oldNodes) {
        var newNodes = new NodeSetMutable();

        for (var oldNode : oldNodes) {
            var newNode = find(oldNode);

            newNodes.add(newNode);
        }

        return newNodes.build();
    }

    public Root find(Root oldRoot) {
        var newSource = find(oldRoot.source);
        var newTargets = find(oldRoot.targets);
        return new Root(newSource, newTargets);
    }

    public Link copy(Link oldLink) {
        var newSource = copy(oldLink.source);
        var newTarget = copy(oldLink.target);

        return newGraph.createLink(
                newSource, newTarget,
                Event.copy(oldLink.event),
                oldLink.symbol,
                oldLink.badge);
    }
}
