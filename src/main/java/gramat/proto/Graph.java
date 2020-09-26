package gramat.proto;

import gramat.symbols.Symbol;
import gramat.util.Count;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {

    private static final Count stateIds = new Count();

    public final List<Edge> edges;
    public final List<Vertex> vertices;

    public Segment entryPoint;

    public Graph() {
        edges = new ArrayList<>();
        vertices = new ArrayList<>();
        entryPoint = new Segment(createVertex(), createVertex());
    }

    public Vertex createVertex() {
        return createVertex(false);
    }

    public VertexSet createVertexSet() {
        var vertex = createVertex();

        return new VertexSet(vertex);
    }

    public Vertex createVertexWild() {
        return createVertex(true);
    }

    public Vertex createVertex(boolean wild) {
        return new Vertex(stateIds.nextString(), wild);
    }

    public void createEdge(VertexSet sources, VertexSet targets, Symbol symbol) {
        for (var source : sources) {
            for (var target : targets) {
                var transition = new EdgeSymbol(source, target, symbol);

                edges.add(transition);
            }
        }
    }

    public void createEdge(VertexSet sources, VertexSet targets, String reference) {
        for (var source : sources) {
            for (var target : targets) {
                var transition = new EdgeReference(source, target, reference);

                edges.add(transition);
            }
        }
    }

    public List<Edge> findEdgesTo(VertexSet targets) {
        return edges.stream()
                .filter(t -> targets.contains(t.target))
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
}
