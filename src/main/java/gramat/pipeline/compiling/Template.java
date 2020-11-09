package gramat.pipeline.compiling;

import gramat.scheme.graph.Graph;
import gramat.scheme.graph.Root;
import gramat.scheme.graph.plugs.Extension;
import gramat.util.NameMap;

public class Template {

    public final Graph graph;
    public final Root main;
    public final NameMap<Extension> extensions;

    public Template(Graph graph, Root main, NameMap<Extension> extensions) {
        this.graph = graph;
        this.main = main;
        this.extensions = extensions;
    }
}
