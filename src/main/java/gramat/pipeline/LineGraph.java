package gramat.pipeline;

import gramat.graph.Line;
import gramat.util.NameMap;

public class LineGraph {

    public final Line main;
    public final NameMap<Line> dependencies;

    public LineGraph(Line main, NameMap<Line> dependencies) {
        this.main = main;
        this.dependencies = dependencies;
    }
}
