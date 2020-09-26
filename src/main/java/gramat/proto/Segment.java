package gramat.proto;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class Segment {

    public final VertexSet sources;
    public final VertexSet targets;

    public Segment(Vertex source, Vertex target) {
        this(new VertexSet(source), new VertexSet(target));
    }

    public Segment(VertexSet sources, VertexSet targets) {
        this.sources = sources;
        this.targets = targets;
    }

    public Segment copy() {
        return new Segment(sources.copy(), targets.copy());
    }

    public Segment copyInverse() {
        return new Segment(targets.copy(), sources.copy());
    }

    public void add(Segment other) {
        this.sources.add(other.sources);
        this.targets.add(other.targets);
    }
}
