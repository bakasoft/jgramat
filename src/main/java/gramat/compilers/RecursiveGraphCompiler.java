package gramat.compilers;

import gramat.actions.ActionStore;
import gramat.badges.Badge;
import gramat.exceptions.UnsupportedValueException;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.graph.*;
import gramat.graph.plugs.*;
import gramat.util.Count;
import gramat.util.NameMap;

import java.util.*;

public class RecursiveGraphCompiler extends DefaultComponent {

    private final Graph graph;
    private final NameMap<Line> oldLines;
    private final Count callCount;

    private final Stack<String> stackRef;
    private final NameMap<Extension> extensions;

    public RecursiveGraphCompiler(Component parent, NameMap<Line> lines) {
        super(parent);
        this.oldLines = lines;
        this.graph = new Graph();
        this.callCount = new Count();
        this.stackRef = new Stack<>();
        this.extensions = new NameMap<>();
    }

    public Line resolve(Line oldLine) {
        var newLine = graph.createLine();

        makeDirectCopy(newLine, oldLine);

        return newLine;
    }

    private void makeDirectCopy(Line newLine, Line oldLine) {
        var newNodes = new HashMap<Node, Node>();

        newNodes.put(oldLine.source, newLine.source);
        newNodes.put(oldLine.target, newLine.target);

        var newLinkRefs = new ArrayList<LinkReference>();

        // Direct copy
        for (var oldLink : oldLine.graph.walkLinksFrom(oldLine.source)) {
            var newLink = makeDirectCopy(graph, oldLink, newNodes);

            if (newLink instanceof LinkReference) {
                newLinkRefs.add((LinkReference)newLink);
            }
        }

        // Resolve copied references
        for (var newLinkRef : newLinkRefs) {
            resolveReference(newLinkRef);
        }
    }

    private Extension makeExtension(LinkReference linkRef) {
        return extensions.computeIfAbsent(linkRef.reference, name -> {
            var oldLine = oldLines.find(name);

            return makeExtension(oldLine);
        });
    }

    private Extension makeExtension(Line oldLine) {
        var newNodes = new HashMap<Node, Node>();
        var plugs = new ArrayList<Plug>();

        for (var oldLink : oldLine.graph.findLinksBetween(oldLine.source, oldLine.target)) {
            var plug = tryMakePlug(oldLink, oldLine, newNodes);

            if (plug != null) {
                plugs.add(plug);
            }
            else {
                makeDirectCopy(graph, oldLink, newNodes);
            }
        }

        return new Extension(plugs);
    }

    private Plug tryMakePlug(Link oldLink, Line oldLine, Map<Node, Node> newNodes) {
        if (oldLink instanceof LinkSymbol) {
            var oldLinkSym = (LinkSymbol)oldLink;
            if (oldLink.source == oldLine.source) {
                // From source
                if (oldLink.target == oldLine.target) {
                    // To target
                    return new PlugSymbolSourceToTarget(oldLinkSym);
                } else {
                    // To Node
                    var newTarget = newNodes.computeIfAbsent(oldLink.target, graph::createNodeFrom);

                    return new PlugSymbolSourceToNode(oldLinkSym, newTarget);
                }
            }
            else if (oldLink.source == oldLine.target) {
                if (oldLink.target == oldLine.source) {
                    return new PlugSymbolTargetToSource(oldLinkSym);
                } else {
                    var newTarget = newNodes.computeIfAbsent(oldLink.target, graph::createNodeFrom);

                    return new PlugSymbolTargetToNode(oldLinkSym, newTarget);
                }
            }
            else if (oldLink.target == oldLine.source) {
                var newSource = newNodes.computeIfAbsent(oldLink.source, graph::createNodeFrom);

                return new PlugSymbolNodeToSource(oldLinkSym, newSource);
            }
            else if (oldLink.target == oldLine.target) {
                var newSource = newNodes.computeIfAbsent(oldLink.source, graph::createNodeFrom);

                return new PlugSymbolNodeToTarget(oldLinkSym, newSource);
            }
        }

        return null;
    }

    private static Link makeDirectCopy(Graph graph, Link oldLink, Map<Node, Node> newNodes) {
        var newSource = newNodes.computeIfAbsent(oldLink.source, graph::createNodeFrom);
        var newTarget = newNodes.computeIfAbsent(oldLink.target, graph::createNodeFrom);

        if (oldLink instanceof LinkSymbol) {
            var oldLinkSym = (LinkSymbol)oldLink;

            return graph.createLink(
                    newSource, newTarget,
                    oldLinkSym.beforeActions, oldLinkSym.afterActions,
                    oldLinkSym.symbol, oldLinkSym.badge, oldLinkSym.mode);
        }
        else if (oldLink instanceof LinkReference) {
            var oldLinkRef = (LinkReference)oldLink;

            return graph.createLink(
                    newSource, newTarget,
                    oldLinkRef.beforeActions, oldLinkRef.afterActions,
                    oldLinkRef.reference);
        }
        else {
            throw new UnsupportedValueException(oldLink);
        }
    }

    private void connectExtensionTo(Badge newBadge, Extension extension, Node source, Node target, ActionStore beforeActions, ActionStore afterActions) {
        for (var plug : extension.plugs) {
            if (plug instanceof PlugSymbol) {
                plug.connectTo(graph, source, target, newBadge, beforeActions, afterActions);
            }
            else {
                throw new UnsupportedValueException(plug);
            }
        }

        // Resolve connected references
        for (var linkRef : graph.findReferencesBetween(source, target)) {
            resolveReference(linkRef);
        }
    }

    private void resolveReference(LinkReference linkRef) {
        var extension = makeExtension(linkRef);
        var reference = linkRef.reference;
        var recursive = stackRef.contains(reference);

        stackRef.push(reference);

        Badge newBadge;

        if (recursive) {
            newBadge = gramat.badges.badge(reference + "-" + callCount.nextString());
        }
        else {
            newBadge = gramat.badges.empty();
        }

        graph.removeLink(linkRef);

        connectExtensionTo(newBadge, extension, linkRef.source, linkRef.target, linkRef.beforeActions, linkRef.afterActions);

        stackRef.pop();
    }

    public Graph getGraph() {
        return graph;
    }
}
