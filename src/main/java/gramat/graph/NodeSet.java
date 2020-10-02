package gramat.graph;

import java.util.*;

public class NodeSet implements Iterable<Node> {

    private final Set<Node> items;

    public NodeSet(Node node) {
        items = new LinkedHashSet<>();
        add(node);
    }

    public NodeSet(NodeSet original) {
        items = new LinkedHashSet<>(original.items);
    }

    public NodeSet(Set<Node> set) {
        items = new LinkedHashSet<>(set);
        if (items.isEmpty()) {
            throw new RuntimeException();
        }
    }

    public void add(Node node) {
        items.add(Objects.requireNonNull(node));
    }

    @Override
    public Iterator<Node> iterator() {
        return items.iterator();
    }

    public void add(NodeSet nodes) {
        items.addAll(nodes.items);
    }

    public NodeSet copy() {
        return new NodeSet(this);
    }

    public boolean contains(Node source) {
        return items.contains(source);
    }

    public Collection<Node> toCollection() {
        return new ArrayList<>(items);
    }

    public boolean containsAny(NodeSet other) {
        for (var v : other.items) {
            if (items.contains(v)) {
                return true;
            }
        }
        return false;
    }

    public String computeID() {
        var ids = items.stream().map(n -> n.id).toArray();

        Arrays.sort(ids);

        var uid = new StringBuilder();

        for (int i = 0; i < ids.length; i++) {
            if (i > 0) {
                uid.append("_");
            }
            uid.append(ids[i]);
        }

        return uid.toString();
    }

}
