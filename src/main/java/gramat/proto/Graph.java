package gramat.proto;

import gramat.symbols.Symbol;
import gramat.util.Count;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static gramat.util.Validations.isAny;

public class Graph {

    private final Count ids;

    public final List<Edge> edges;
    public final List<Vertex> vertices;

    public Graph() {
        this(new Count());
    }

    public Graph(Count ids) {
        this.ids = ids;
        this.edges = new ArrayList<>();
        this.vertices = new ArrayList<>();
    }

    public Vertex merge(Vertex left, Vertex right) {
        if (left == right) {
            return left;
        }
        var id = String.join("_", left.id, right.id);
        var result = createVertex(id);

        for (var edge : edges) {
            if (isAny(edge.source, left, right)) {
                edge.source = result;
            }
            if (isAny(edge.target, left, right)) {
                edge.target = result;
            }
        }

        if (!vertices.remove(left)) {
            throw new RuntimeException();
        }
        if (!vertices.remove(right)) {
            throw new RuntimeException();
        }

        return result;
    }

    public Vertex createVertex() {
        var id = ids.nextString();
        return createVertex(id, false);
    }

    public Vertex createVertex(String id) {
        return createVertex(id, false);
    }

    public Vertex createVertex(String id, boolean wild) {
        var vertex = new Vertex(id, wild);

        vertices.add(vertex);

        return vertex;
    }

    public VertexSet createVertexSet() {
        var vertex = createVertex();

        return new VertexSet(vertex);
    }

    public void createEdge(Vertex source, Vertex target, Symbol symbol) {
        edges.add(new EdgeSymbol(source, target, symbol));
    }

    public void createEdges(VertexSet sources, VertexSet targets, Symbol symbol) {
        create_edges(sources, targets, (source, target) -> new EdgeSymbol(source, target, symbol));
    }

    public void createEdges(VertexSet sources, VertexSet targets, String reference) {
        create_edges(sources, targets, (source, target) -> new EdgeReference(source, target, reference));
    }

    private void create_edges(Iterable<Vertex> sources, Iterable<Vertex> targets, BiFunction<Vertex, Vertex, Edge> edgeMaker) {
        for (var source : sources) {
            for (var target : targets) {
                var edge = edgeMaker.apply(source, target);

                edges.add(edge);
            }
        }
    }

    public List<Edge> findEdgesTo(VertexSet targets) {
        return edges.stream()
                .filter(t -> targets.contains(t.target))
                .collect(Collectors.toList());
    }

    public List<Edge> findEdgesTo(Vertex target) {
        return edges.stream()
                .filter(t -> t.target == target)
                .collect(Collectors.toList());
    }

    public List<Edge> findEdgesFrom(VertexSet sources, Symbol symbol) {
        return edges.stream()
                .filter(e -> sources.contains(e.source))
                .map(e -> (EdgeSymbol)e)  // TODO add validation for this casting
                .filter(e -> Objects.equals(e.symbol, symbol))
                .collect(Collectors.toList());
    }

    public List<Edge> findEdgesFrom(VertexSet sources) {
        return edges.stream()
                .filter(t -> sources.contains(t.source))
                .collect(Collectors.toList());
    }

    public List<Edge> findEdgesFrom(Vertex source) {
        return edges.stream()
                .filter(t -> t.source == source)
                .collect(Collectors.toList());
    }

    public Set<String> findReferences() {
        var refs = new LinkedHashSet<String>();

        for (var edge : edges) {
            if (edge instanceof EdgeReference) {
                refs.add(((EdgeReference)edge).name);
            }
        }

        return refs;
    }

    public List<Edge> listEdgesFrom(Vertex source) {
        return listEdgesFrom(new VertexSet(source));
    }

    public List<Edge> listEdgesTo(Vertex target) {
        return listEdgesTo(new VertexSet(target));
    }

    public List<Edge> listEdgesFrom(VertexSet sources) {
        var result = new ArrayList<Edge>();
        var control = new HashSet<Vertex>();
        var queue = new LinkedList<Vertex>();

        queue.addAll(sources.toCollection());

        while (queue.size() > 0) {
            var source = queue.remove();

            if (control.add(source)) {
                for (var edge : findEdgesFrom(source)) {
                    result.add(edge);

                    queue.add(edge.target);
                }
            }
        }

        return result;
    }

    public List<Edge> listEdgesTo(VertexSet targets) {
        var result = new ArrayList<Edge>();
        var control = new HashSet<Vertex>();
        var queue = new LinkedList<Vertex>();

        queue.addAll(targets.toCollection());

        while (queue.size() > 0) {
            var target = queue.remove();

            if (control.add(target)) {
                for (var edge : findEdgesTo(target)) {
                    result.add(edge);

                    queue.add(edge.target);
                }
            }
        }

        return result;
    }

    public Segment segment() {
        return new Segment(this, createVertexSet(), createVertexSet());
    }

    public Segment segment(Vertex source, Vertex target) {
        return new Segment(this, new VertexSet(source), new VertexSet(target));
    }

    public Segment segment(VertexSet sources, VertexSet targets) {
        return new Segment(this, sources, targets);
    }

    public Line createLine() {
        return new Line(createVertex(), createVertex());
    }
}
