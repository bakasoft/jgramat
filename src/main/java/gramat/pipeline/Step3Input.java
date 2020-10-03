package gramat.pipeline;

import gramat.framework.Component;
import gramat.graph.Line;
import gramat.util.NameMap;

public class Step3Input {

    public final Component parent;
    public final Line main;
    public final NameMap<Line> dependencies;

    public Step3Input(Component parent, Line main, NameMap<Line> dependencies) {
        this.parent = parent;
        this.main = main;
        this.dependencies = dependencies;
    }
}
