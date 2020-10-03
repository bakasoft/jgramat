package gramat.pipeline;

import gramat.actions.ActionStore;
import gramat.graph.*;
import gramat.util.NameMap;

import java.util.*;

public class LineReducer {

    private final Graph graph;
    private final NameMap<Line> lines;

    public LineReducer(NameMap<Line> lines) {
        this.lines = lines;
        this.graph = new Graph();
    }

    public Line resolve(Line line) {
        var extension = make_extension_from(graph, line);
        var extensions = make_extensions_from(graph, lines);
        var root = graph.createLine();

        connect_extension_to(extension, root.source, root.target, null, null);

        var loop = true;
        while (loop) {
            loop = false;
            for (var link : graph.walkLinksFrom(root.source)) {
                if (link.token.isReference()) {
                    var refName = link.token.getReference();
                    var refExt = extensions.find(refName);

                    connect_extension_to(refExt, link.source, link.target, link.beforeActions, link.afterActions);

                    graph.removeLink(link);
                    loop = true;
                }
            }
        }

        return root;
    }

    private static NameMap<Extension> make_extensions_from(Graph graph, NameMap<Line> lines) {
        var exts = new NameMap<Extension>();

        for (var entry : lines.entrySet()) {
            var name = entry.getKey();
            var line = entry.getValue();
            var ext = make_extension_from(graph, line);

            exts.put(name, ext);
        }

        return exts;
    }

    private static Extension make_extension_from(Graph graph, Line original) {
        var newNodes = new HashMap<Node, Node>();
        var plugs = new ArrayList<Plug>();

        for (var link : original.graph.walkLinksFrom(original.source)) {
            if (link.source == original.source) {
                if (link.target == original.target) {
                    plugs.add(Plug.createFromSourceToTarget(link));
                }
                else {
                    var newTarget = newNodes.computeIfAbsent(link.target, graph::createNodeFrom);

                    plugs.add(Plug.createFromSourceToNode(link, newTarget));
                }
            }
            else if (link.source == original.target) {
                if (link.target == original.source) {
                    plugs.add(Plug.createFromTargetToSource(link));
                }
                else {
                    var newTarget = newNodes.computeIfAbsent(link.target, graph::createNodeFrom);

                    plugs.add(Plug.createFromTargetToNode(link, newTarget));
                }
            }
            else if (link.target == original.source) {
                var newSource = newNodes.computeIfAbsent(link.source, graph::createNodeFrom);

                plugs.add(Plug.createFromNodeToSource(link, newSource));
            }
            else if (link.target == original.target) {
                var newSource = newNodes.computeIfAbsent(link.source, graph::createNodeFrom);

                plugs.add(Plug.createFromNodeToTarget(link, newSource));
            }
            else {
                var newSource = newNodes.computeIfAbsent(link.source, graph::createNodeFrom);
                var newTarget = newNodes.computeIfAbsent(link.target, graph::createNodeFrom);

                graph.createLinkFrom(link, newSource, newTarget);
            }
        }

        return new Extension(plugs);
    }

    public void connect_extension_to(Extension extension, Node source, Node target, ActionStore beforeActions, ActionStore afterActions) {
        for (var plug : extension.plugs) {
            Link link;

            if (plug.type == Plug.FROM_SOURCE_TO_TARGET) {
                link = graph.createLinkFrom(plug, source, target);
            }
            else if (plug.type == Plug.FROM_SOURCE_TO_NODE) {
                link = graph.createLinkFrom(plug, source, plug.getNode());
            }
            else if (plug.type == Plug.FROM_NODE_TO_SOURCE) {
                link = graph.createLinkFrom(plug, plug.getNode(), source);
            }
            else if (plug.type == Plug.FROM_NODE_TO_TARGET) {
                link = graph.createLinkFrom(plug, plug.getNode(), target);
            }
            else if (plug.type == Plug.FROM_TARGET_TO_NODE) {
                link = graph.createLinkFrom(plug, target, plug.getNode());
            }
            else if (plug.type == Plug.FROM_TARGET_TO_SOURCE) {
                link = graph.createLinkFrom(plug, target, source);
            }
            else {
                throw new RuntimeException();
            }

            if (beforeActions != null) {
                link.beforeActions.prepend(beforeActions);
            }

            if (afterActions != null) {
                link.afterActions.append(afterActions);
            }
        }
    }

    public Graph getGraph() {
        return graph;
    }
}
