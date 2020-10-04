package gramat.compilers;

import gramat.actions.ActionStore;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.exceptions.UnsupportedValueException;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.graph.*;
import gramat.util.Count;
import gramat.util.NameMap;

import java.beans.Expression;
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

        for (var oldLink : oldLine.graph.walkLinksFrom(oldLine.source)) {
            if (oldLink.source == oldLine.source) {
                if (oldLink.target == oldLine.target) {
                    plugs.add(Plug.createFromSourceToTarget(oldLink));
                }
                else {
                    var newTarget = newNodes.computeIfAbsent(oldLink.target, graph::createNodeFrom);

                    plugs.add(Plug.createFromSourceToNode(oldLink, newTarget));
                }
            }
            else if (oldLink.source == oldLine.target) {
                if (oldLink.target == oldLine.source) {
                    plugs.add(Plug.createFromTargetToSource(oldLink));
                }
                else {
                    var newTarget = newNodes.computeIfAbsent(oldLink.target, graph::createNodeFrom);

                    plugs.add(Plug.createFromTargetToNode(oldLink, newTarget));
                }
            }
            else if (oldLink.target == oldLine.source) {
                var newSource = newNodes.computeIfAbsent(oldLink.source, graph::createNodeFrom);

                plugs.add(Plug.createFromNodeToSource(oldLink, newSource));
            }
            else if (oldLink.target == oldLine.target) {
                var newSource = newNodes.computeIfAbsent(oldLink.source, graph::createNodeFrom);

                plugs.add(Plug.createFromNodeToTarget(oldLink, newSource));
            }
            else {
                makeDirectCopy(graph, oldLink, newNodes);
            }
        }

        return new Extension(plugs);
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
            if (plug.link instanceof LinkSymbol) {
                var oldLink = (LinkSymbol)plug.link;
                var newLine = computeLineFromPlug(plug, source, target);
                var newMode = computeBadgeModeFromPlug(plug, newBadge);
                var newLink = graph.createLink(
                        newLine.source, newLine.target,
                        oldLink.beforeActions, oldLink.afterActions,
                        oldLink.symbol, newBadge, newMode);

                // Apply wrapping actions
                if (beforeActions != null) {
                    newLink.beforeActions.prepend(beforeActions);
                }

                if (afterActions != null) {
                    newLink.afterActions.append(afterActions);
                }
            }
            else {
                throw new UnsupportedValueException(plug.link);
            }
        }

        // Resolve connected references
        for (var linkRef : graph.findReferences(source, target)) {
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

    private Line computeLineFromPlug(Plug plug, Node source, Node target) {
        if (plug.type == Plug.FROM_SOURCE_TO_TARGET) {
            return new Line(graph, source, target);
        }
        else if (plug.type == Plug.FROM_SOURCE_TO_NODE) {
            return new Line(graph, source, plug.node);
        }
        else if (plug.type == Plug.FROM_NODE_TO_SOURCE) {
            return new Line(graph, plug.node, source);
        }
        else if (plug.type == Plug.FROM_NODE_TO_TARGET) {
            return new Line(graph, plug.node, target);
        }
        else if (plug.type == Plug.FROM_TARGET_TO_NODE) {
            return new Line(graph, target, plug.node);
        }
        else if (plug.type == Plug.FROM_TARGET_TO_SOURCE) {
            return new Line(graph, target, source);
        }
        else {
            throw new UnsupportedValueException(plug.type, "plug type");
        }
    }

    private BadgeMode computeBadgeModeFromPlug(Plug plug, Badge badge) {
        if (badge == gramat.badges.empty()) {
            // If the badge is the empty, there is no point to have a badge mode
            return BadgeMode.NONE;
        }
        else if (plug.type == Plug.FROM_SOURCE_TO_TARGET) {
            return BadgeMode.NONE;
        }
        else if (plug.type == Plug.FROM_SOURCE_TO_NODE) {
            return BadgeMode.PUSH;
        }
        else if (plug.type == Plug.FROM_NODE_TO_SOURCE) {
            return BadgeMode.POP;
        }
        else if (plug.type == Plug.FROM_NODE_TO_TARGET) {
            return BadgeMode.POP;
        }
        else if (plug.type == Plug.FROM_TARGET_TO_NODE) {
            return BadgeMode.PUSH;
        }
        else if (plug.type == Plug.FROM_TARGET_TO_SOURCE) {
            return BadgeMode.NONE;
        }
        else {
            throw new UnsupportedValueException(plug.type, "plug type");
        }
    }

    public Graph getGraph() {
        return graph;
    }
}
