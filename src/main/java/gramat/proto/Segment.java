package gramat.proto;

public class Segment {

    public final Graph graph;
    public final NodeSet sources;
    public final NodeSet targets;

    public Segment(Graph graph, NodeSet sources, NodeSet targets) {
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
