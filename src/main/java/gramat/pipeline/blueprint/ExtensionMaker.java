package gramat.pipeline.blueprint;

import gramat.graph.Graph;
import gramat.graph.Root;
import gramat.graph.plugs.Extension;
import gramat.graph.plugs.Plug;
import gramat.graph.plugs.PlugType;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class ExtensionMaker {

    public static Extension make(Graph graph, Root root, int id) {
        var plugs = new LinkedHashSet<Plug>();

        for (var link : graph.findLinksBetween(root)) {
            var type = PlugType.compute(root, link);

            if (type != PlugType.NODE_TO_NODE) {
                var plug = Plug.make(link, type);

                plugs.add(plug);

                // The link is replaced by the plug
                graph.removeLink(link);
            }
        }

        return new Extension(id, plugs);
    }

}
