package gramat.pipeline;

import gramat.graph.Graph;
import gramat.graph.Root;
import gramat.util.NameMap;

public class Blueprint {

    public final Graph graph;
    public final Root root;
    public final NameMap<Root> dependencies;

    public Blueprint(Graph graph, Root root, NameMap<Root> dependencies) {
        this.graph = graph;
        this.root = root;
        this.dependencies = dependencies;
    }
}
