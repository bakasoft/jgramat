package gramat.compiling;

import gramat.actions.ActionStore;
import gramat.graph.*;
import gramat.util.NameMap;

import java.util.*;

public class SegmentFlattener {

    private final NameMap<Segment> segments;

    public SegmentFlattener(NameMap<Segment> segments) {
        this.segments = segments;
    }

    public NameMap<Line> flatten(String name) {
        var result = new NameMap<Line>();

        var recursive = compute_recursive_segments(name);

        result.put(name, make_line(name, recursive));

        for (var depName : recursive) {
            if (!result.containsKey(depName)) {
                result.put(depName, make_line(depName, recursive));
            }
        }

        return result;
    }

    private Line make_line(String name, Set<String> recursive) {
        var graph = new Graph();
        var depSegment = segments.find(name);
        var depLine = graph.createLine();

        copy_segment(graph, depSegment, depLine.source, depLine.target, new ActionStore(), new ActionStore(), recursive);

        return depLine;
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

            if (link.token.isSymbol() || recursive.contains(link.token.getReference())) {
                var linkCopy = graph.createLink(sourceCopy, targetCopy, link.token);

                linkCopy.afterActions.append(link.afterActions);
                linkCopy.beforeActions.append(link.beforeActions);
            }
            else {
                var refSegment = segments.find(link.token.getReference());

                copy_segment(
                        graph,
                        refSegment,
                        sourceCopy, targetCopy,
                        link.beforeActions, link.afterActions, recursive);
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

    private Set<String> compute_recursive_segments(String root) {
        var result = new LinkedHashSet<String>();

        compute_recursive_segments(root, new Stack<>(), result);

        return result;
    }

    private void compute_recursive_segments(String name, Stack<String> stack, Set<String> result) {
        var segment = segments.find(name);

        stack.push(name);

        for (var link : segment.graph.walkLinksFrom(segment.sources)) {
            if (link.token.isReference()) {
                var refName = link.token.getReference();

                if (stack.contains(refName)) {
                    result.add(refName);
                }
                else {
                    compute_recursive_segments(refName, stack, result);
                }
            }
        }

        stack.pop();
    }

}
