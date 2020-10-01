package gramat.proto;

import java.util.*;

public class VertexSet implements Iterable<Vertex> {

    private final Set<Vertex> items;

    public VertexSet(Vertex vertex) {
        items = new LinkedHashSet<>();
        add(vertex);
    }

    public VertexSet(VertexSet original) {
        items = new LinkedHashSet<>(original.items);
    }

    public VertexSet(Set<Vertex> set) {
        items = new LinkedHashSet<>(set);
        if (items.isEmpty()) {
            throw new RuntimeException();
        }
    }

    public void add(Vertex vertex) {
        items.add(Objects.requireNonNull(vertex));
    }

    @Override
    public Iterator<Vertex> iterator() {
        return items.iterator();
    }

    public void add(VertexSet vertices) {
        items.addAll(vertices.items);
    }

    public VertexSet copy() {
        return new VertexSet(this);
    }

    public boolean contains(Vertex source) {
        return items.contains(source);
    }

    public Collection<Vertex> toCollection() {
        return new ArrayList<>(items);
    }

    public boolean containsAny(VertexSet other) {
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
