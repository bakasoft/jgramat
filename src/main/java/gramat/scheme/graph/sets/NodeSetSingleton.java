package gramat.scheme.graph.sets;

import gramat.scheme.graph.Node;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class NodeSetSingleton implements NodeSet {
    private final Node node;

    public NodeSetSingleton(Node node) {
        this.node = node;
    }

    @Override
    public boolean contains(Node node) {
        return Objects.equals(this.node, node);
    }

    @Override
    public List<Node> toList() {
        return List.of(node);
    }

    @Override
    public boolean containsAny(NodeSet nodes) {
        return nodes.contains(node);
    }

    @Override
    public boolean anyMatch(Predicate<Node> condition) {
        return condition.test(node);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public Iterator<Node> iterator() {
        return List.of(node).iterator();
    }
}
