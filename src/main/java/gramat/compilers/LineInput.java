package gramat.compilers;

import gramat.framework.Component;
import gramat.graph.Line;
import gramat.util.NameMap;

public class LineInput {

    public final Component parent;
    public final Line main;
    public final NameMap<Line> dependencies;

    public LineInput(Component parent, Line main, NameMap<Line> dependencies) {
        this.parent = parent;
        this.main = main;
        this.dependencies = dependencies;
    }
}
