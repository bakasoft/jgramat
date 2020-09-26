package gramat.proto;

import java.util.*;
import java.util.stream.Collectors;

public class VertexSet implements Iterable<Vertex> {

    private final Set<Vertex> items;

    public VertexSet(Vertex vertex) {
        items = new LinkedHashSet<>();
        add(vertex);
    }

    public VertexSet(VertexSet original) {
        items = new LinkedHashSet<>(original.items);
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
}
