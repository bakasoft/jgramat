package gramat.pipeline.compiling;

import gramat.scheme.models.Graph;
import gramat.scheme.models.Root;
import gramat.scheme.models.plugs.Extension;
import gramat.scheme.models.plugs.Plug;
import gramat.scheme.models.plugs.PlugType;

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
