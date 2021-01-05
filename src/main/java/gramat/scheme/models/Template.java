package gramat.scheme.models;

import gramat.scheme.models.Graph;
import gramat.scheme.models.Root;
import gramat.scheme.models.plugs.Extension;
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
