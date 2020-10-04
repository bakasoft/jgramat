package gramat.compilers;

import gramat.actions.Action;
import gramat.actions.ActionStore;
import gramat.actions.RecursionEnter;
import gramat.actions.RecursionExit;
import gramat.badges.BadgeMode;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.graph.*;
import gramat.util.Count;
import gramat.util.NameMap;

import java.util.*;

public class RecursiveGraphCompiler extends DefaultComponent {

    private final Graph graph;
    private final NameMap<Line> lines;
    private final Count callCount;

    public RecursiveGraphCompiler(Component parent, NameMap<Line> lines) {
        super(parent);
        this.lines = lines;
        this.graph = new Graph();
        this.callCount = new Count();
    }

    public Line resolve(Line line) {
        var extension = make_extension_from(graph, line);
        var extensions = make_extensions_from(graph, lines);
        var root = graph.createLine();

        connect_extension_to(null, extension, root.source, root.target, null, null);

        var loop = true;
        while (loop) {
            loop = false;
            for (var link : graph.walkLinksFrom(root.source)) {
                if (link instanceof LinkReference) {
                    var refLink = (LinkReference)link;
                    var refName = refLink.reference;
                    var refExt = extensions.find(refName);

                    connect_extension_to(refName, refExt, link.source, link.target, link.beforeActions, link.afterActions);

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

                if (link instanceof LinkSymbol) {
                    var linkSymbol = (LinkSymbol)link;

                    graph.createLink(
                            newSource, newTarget,
                            linkSymbol.beforeActions, linkSymbol.afterActions,
                            linkSymbol.symbol, linkSymbol.badge, linkSymbol.mode);
                }
                else if (link instanceof LinkReference) {
                    var linkRef = (LinkReference)link;

                    graph.createLink(
                            newSource, newTarget,
                            linkRef.beforeActions, linkRef.afterActions,
                            linkRef.reference);
                }
                else {
                    throw new RuntimeException();
                }
            }
        }

        return new Extension(plugs);
    }

    public void connect_extension_to(String name, Extension extension, Node source, Node target, ActionStore beforeActions, ActionStore afterActions) {
        var newBadge = name != null ? gramat.badges.badge(name + "-" + callCount.nextString()) : null;

        for (var plug : extension.plugs) {
            Node newSource;
            Node newTarget;
            BadgeMode mode;

            if (plug.type == Plug.FROM_SOURCE_TO_TARGET) {
                newSource = source;
                newTarget = target;
                mode = BadgeMode.NONE;
            }
            else if (plug.type == Plug.FROM_SOURCE_TO_NODE) {
                newSource = source;
                newTarget = plug.node;
                mode = BadgeMode.PUSH;
            }
            else if (plug.type == Plug.FROM_NODE_TO_SOURCE) {
                newSource = plug.node;
                newTarget = source;
                mode = BadgeMode.POP;
            }
            else if (plug.type == Plug.FROM_NODE_TO_TARGET) {
                newSource = plug.node;
                newTarget = target;
                mode = BadgeMode.POP;
            }
            else if (plug.type == Plug.FROM_TARGET_TO_NODE) {
                newSource = target;
                newTarget = plug.node;
                mode = BadgeMode.PUSH;
            }
            else if (plug.type == Plug.FROM_TARGET_TO_SOURCE) {
                newSource = target;
                newTarget = source;
                mode = BadgeMode.NONE;
            }
            else {
                throw new RuntimeException();
            }

            var newLinks = new ArrayList<Link>();  // TODO check if it is only one...

            if (plug.link instanceof LinkSymbol) {
                var linkSym = (LinkSymbol)plug.link;

                // TODO this probably is required for non-empty badges
                // newLinks.add(graph.createLink(
                //         newSource, newTarget,
                //         linkSym.beforeActions, linkSym.afterActions,
                //         linkSym.symbol, linkSym.badge));

                newLinks.add(graph.createLink(
                        newSource, newTarget,
                        linkSym.beforeActions, linkSym.afterActions,
                        linkSym.symbol, newBadge, mode));
            }
            else if (plug.link instanceof LinkReference) {
                var linkRef = (LinkReference)plug.link;

                newLinks.add(graph.createLink(
                        newSource, newTarget,
                        linkRef.beforeActions, linkRef.afterActions,
                        linkRef.reference));  // TODO should this have badge?
            }
            else {
                throw new RuntimeException();
            }

            // Apply wrapping actions

            for (var newLink : newLinks) {
                if (beforeActions != null) {
                    newLink.beforeActions.prepend(beforeActions);
                }

                if (afterActions != null) {
                    newLink.afterActions.append(afterActions);
                }
            }
        }
    }

    public Graph getGraph() {
        return graph;
    }
}
