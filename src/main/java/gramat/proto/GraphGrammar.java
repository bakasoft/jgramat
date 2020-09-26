package gramat.proto;

import java.util.*;

public class GraphGrammar {

    private final Map<String, Graph> graphs;

    public GraphGrammar() {
        graphs = new LinkedHashMap<>();
    }

    public Graph findGraph(String name) {
        var graph = graphs.get(name);

        if (graph == null) {
            throw new RuntimeException("Graph not found: " + name);
        }

        return graph;
    }

    public Graph createGraph(String name) {
        var graph = new Graph();

        // TODO check duplicates

        graphs.put(name, graph);

        return graph;
    }

    public boolean isFlat(Graph origin) {
        var control = new HashSet<Graph>();
        var queue = new LinkedList<Graph>();

        queue.add(origin);

        while (queue.size() > 0) {
            var graph = queue.remove();

            if (control.add(graph)) {
                for (var refName : graph.findReferences()) {
                    var refGraph = findGraph(refName);

                    if (refGraph == origin) {
                        return false;
                    }

                    queue.add(refGraph);
                }
            }
        }

        return true;
    }

    public Graph searchGraph(String name) {
        return graphs.get(name);
    }
}
