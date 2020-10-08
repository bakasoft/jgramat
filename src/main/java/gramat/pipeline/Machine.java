package gramat.pipeline;

import gramat.graph.Graph;
import gramat.graph.Root;

public class Machine {

    public final Graph graph;
    public final Root root;

    public Machine(Graph graph, Root root) {
        this.graph = graph;
        this.root = root;
    }
}
