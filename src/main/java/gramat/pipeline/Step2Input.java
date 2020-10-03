package gramat.pipeline;

import gramat.framework.Component;
import gramat.graph.Segment;
import gramat.util.NameMap;

public class Step2Input {

    public final Component parent;
    public final Segment main;
    public final NameMap<Segment> dependencies;

    public Step2Input(Component parent, Segment main, NameMap<Segment> dependencies) {
        this.parent = parent;
        this.main = main;
        this.dependencies = dependencies;
    }
}
