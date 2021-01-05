package gramat.scheme.models.sets;

import gramat.scheme.models.Node;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class NodeSetEmpty implements NodeSet {

    @Override
    public boolean contains(Node node) {
        return false;
    }

    @Override
    public List<Node> toList() {
        return List.of();
    }

    @Override
    public boolean containsAny(NodeSet nodes) {
        return false;
    }

    @Override
    public boolean anyMatch(Predicate<Node> condition) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterator<Node> iterator() {
        return Collections.emptyIterator();
    }
}
