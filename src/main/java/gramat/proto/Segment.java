package gramat.proto;

import java.util.Set;

public class Segment {

    public final Graph graph;
    public final VertexSet sources;
    public final VertexSet targets;

    public Segment(Graph graph, VertexSet sources, VertexSet targets) {
        this.graph = graph;
        this.sources = sources;
        this.targets = targets;
    }

    public Segment shallowCopy() {
        return new Segment(graph, sources.copy(), targets.copy());
    }

    public Segment shallowCopyInverse() {
        return new Segment(graph, targets.copy(), sources.copy());
    }

    public void add(Segment other) {
        this.sources.add(other.sources);
        this.targets.add(other.targets);
    }
}
