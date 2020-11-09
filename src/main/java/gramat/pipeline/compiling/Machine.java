package gramat.pipeline.compiling;

import gramat.scheme.graph.Graph;
import gramat.scheme.graph.Root;
import gramat.scheme.core.symbols.SymbolReference;

public class Machine {

    public final Graph graph;
    public final Root root;

    public Machine(Graph graph, Root root) {
        this.graph = graph;
        this.root = root;
    }

    public void validate() {
        // Validate that all references were resolved
        for (var link : graph.findLinksBetween(root)) {
            if (link.symbol instanceof SymbolReference) {
                throw new RuntimeException("unexpected link reference: " + link);
            }
        }
    }
}
