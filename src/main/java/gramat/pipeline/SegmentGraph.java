package gramat.pipeline;

import gramat.framework.Component;
import gramat.graph.Segment;
import gramat.util.NameMap;

public class SegmentGraph {

    public final Component parent;
    public final Segment main;
    public final NameMap<Segment> dependencies;

    public SegmentGraph(Component parent, Segment main, NameMap<Segment> dependencies) {
        this.parent = parent;
        this.main = main;
        this.dependencies = dependencies;
    }
}
