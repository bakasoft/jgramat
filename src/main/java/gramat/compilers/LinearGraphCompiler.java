package gramat.compilers;

import gramat.actions.ActionStore;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.graph.*;
import gramat.util.NameMap;

import java.util.*;

public class LinearGraphCompiler extends DefaultComponent {

    private final NameMap<Segment> segments;

    public LinearGraphCompiler(Component parent, NameMap<Segment> segments) {
        super(parent);
        this.segments = segments;
    }

    public LineInput compile(Segment segment) {
        var recursive = new LinkedHashSet<String>();

        compute_recursive_dependencies(segment, new Stack<>(), recursive);

        var main = make_line(segment, recursive);
        var dependencies = new NameMap<Line>();

        flatten_segment(dependencies, recursive);

        return new LineInput(gramat, main, dependencies);
    }

    public void flatten_segment(NameMap<Line> result, Set<String> recursive) {
        for (var name : recursive) {
            if (!result.containsKey(name)) {
                var segment = segments.find(name);

                result.put(name, make_line(segment, recursive));
            }
        }
    }

    private Line make_line(Segment segment, Set<String> recursive) {
        var graph = new Graph();
        var line = graph.createLine();

        copy_segment(graph, segment, line.source, line.target, new ActionStore(), new ActionStore(), recursive);

        return line;
    }

    private void copy_segment(Graph graph, Segment segment, Node rootSource, Node rootTarget, ActionStore beforeActions, ActionStore afterActions, Set<String> recursive) {
        var copies = new HashMap<Node, Node>();

        for (var source : segment.sources) {
            copies.put(source, rootSource);
        }

        for (var target : segment.targets) {
            copies.put(target, rootTarget);
        }

        for (var link : segment.graph.walkLinksFrom(segment.sources)) {
            var sourceCopy = copies.computeIfAbsent(link.source, graph::createNodeFrom);
            var targetCopy = copies.computeIfAbsent(link.target, graph::createNodeFrom);

            if (link instanceof LinkSymbol) {
                var linkSymbol = (LinkSymbol)link;

                graph.createLink(
                        sourceCopy, targetCopy,
                        linkSymbol.beforeActions, linkSymbol.afterActions,
                        linkSymbol.symbol, linkSymbol.badge, linkSymbol.mode);
            }
            else if (link instanceof LinkReference) {
                var linkRef = (LinkReference)link;
                if (recursive.contains(linkRef.reference)) {
                    graph.createLink(
                            sourceCopy, targetCopy,
                            linkRef.beforeActions, linkRef.afterActions,
                            linkRef.reference);
                } else {
                    var refSegment = segments.find(linkRef.reference);

                    copy_segment(
                            graph,
                            refSegment,
                            sourceCopy, targetCopy,
                            linkRef.beforeActions, linkRef.afterActions, recursive);
                }
            }
            else {
                throw new RuntimeException();
            }
        }

        // Apply wrapping actions

        for (var entry : copies.entrySet()) {
            var orig = entry.getKey();
            var copy = entry.getValue();

            if (segment.sources.contains(orig)) {
                for (var link : graph.findOutgoingLinks(copy)) {
                    // If the link belongs to the section being copied...
                    if (copies.containsValue(link.target)) {
                        link.beforeActions.append(beforeActions); // TODO double-check order
                    }
                }
            }

            if (segment.targets.contains(orig)) {
                for (var link : graph.findIncomingLinks(copy)) {
                    // If the link belongs to the section being copied...
                    if (copies.containsValue(link.source)) {
                        link.afterActions.append(afterActions); // TODO double-check order
                    }
                }
            }
        }
    }

    private void compute_recursive_dependencies(String name, Segment segment, Stack<String> stack, Set<String> result) {
        stack.push(name);

        compute_recursive_dependencies(segment, stack, result);

        stack.pop();
    }

    private void compute_recursive_dependencies(Segment segment, Stack<String> stack, Set<String> result) {
        for (var link : segment.graph.walkLinksFrom(segment.sources)) {
            if (link instanceof LinkReference) {
                var linkRef = (LinkReference)link;
                var nameRef = linkRef.reference;

                if (stack.contains(nameRef)) {
                    result.add(nameRef);
                }
                else {
                    var refSegment = segments.find(nameRef);

                    compute_recursive_dependencies(nameRef, refSegment, stack, result);
                }
            }
        }
    }

}
