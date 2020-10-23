package gramat.graph.sets;

import gramat.graph.Node;

import java.util.*;
import java.util.function.Predicate;

class NodeSetReadOnly implements NodeSet {

    protected final Set<Node> nodes;

    NodeSetReadOnly(List<Node> nodes) {
        this.nodes = new LinkedHashSet<>(nodes);
    }

    NodeSetReadOnly(Set<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    public boolean contains(Node node) {
        return nodes.contains(node);
    }

    public List<Node> toList() {
        return new ArrayList<>(nodes);
    }

    public boolean containsAny(NodeSet nodes) {
        for (var node : nodes) {
            if (this.nodes.contains(node)) {
                return true;
            }
        }
        return false;
    }

    public boolean anyMatch(Predicate<Node> condition) {
        return nodes.stream().anyMatch(condition);
    }

    public int size() {
        return nodes.size();
    }

}
