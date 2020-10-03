package gramat.pipeline;

import gramat.graph.Segment;
import gramat.util.NameMap;

public class SegmentGraph {

    public final Segment main;
    public final NameMap<Segment> dependencies;

    public SegmentGraph(Segment main, NameMap<Segment> dependencies) {
        this.main = main;
        this.dependencies = dependencies;
    }
}
