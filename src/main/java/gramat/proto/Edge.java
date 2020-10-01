package gramat.proto;

import gramat.actions.Action;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

abstract public class Edge {

    public Vertex source;
    public Vertex target;

    public final List<Action> beforeActions;
    public final List<Action> afterActions;

    public Edge(Vertex source, Vertex target) {
        this.source = Objects.requireNonNull(source);
        this.target = Objects.requireNonNull(target);
        this.beforeActions = new ArrayList<>();
        this.afterActions = new ArrayList<>();
    }

    public static VertexSet collectTargets(Iterable<? extends Edge> edges) {
        var targets = new LinkedHashSet<Vertex>();

        for (var edge : edges) {
            targets.add(edge.target);
        }

        return new VertexSet(targets);
    }

}
