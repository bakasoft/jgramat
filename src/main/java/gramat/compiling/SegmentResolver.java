package gramat.compiling;

import gramat.actions.RecursionExit;
import gramat.actions.RecursionEnter;
import gramat.proto.*;

import java.util.*;

import static gramat.util.Validations.isAny;
import static gramat.util.Validations.requireEquals;

public class SegmentResolver {

    private final Graph root;
    private final SegmentMap segments;

    private final Map<String, Line> namedLines;
    private final Map<Node, Node> copiedNodes;
    private final Map<Line, String> mergeEnter;
    private final Map<Line, String> mergeExit;

    public SegmentResolver(SegmentMap segments) {
        this.segments = segments;
        this.root = new Graph();
        this.namedLines = new HashMap<>();
        this.copiedNodes = new HashMap<>();
        this.mergeEnter = new LinkedHashMap<>();
        this.mergeExit = new LinkedHashMap<>();
    }

    public Line resolve(String name) {
        var original = segments.find(name);

        return resolve(name, original);
    }

    public Line resolve(String name, Segment original) {
        var beforeMerge = new ArrayList<Runnable>();
        var afterMerge = new ArrayList<Runnable>();
        var line = resolve_segment(name, original, beforeMerge, afterMerge);

        for (var step : beforeMerge) {
            step.run();
        }

        // Join recursive segments

        for (var merge : mergeEnter.keySet()) {
            merge_nodes(merge.source, merge.target);
        }

        for (var merge : mergeExit.keySet()) {
            merge_nodes(merge.source, merge.target);
        }

        for (var step : afterMerge) {
            step.run();
        }

        return line;
    }

    private Line resolve_segment(String name, Segment original, List<Runnable> beforeMerge, List<Runnable> afterMerge) {
        var line = namedLines.get(name);

        if (line != null) {
            return line;
        }

        line = root.createLine();

        map_segment(name, original, line);

        copy_segment(original, beforeMerge, afterMerge);

        unmap_segment(name, original);

        return line;
    }

    private void copy_segment(Segment originalSegment, List<Runnable> beforeMerge, List<Runnable> afterMerge) {
        for (var originalLink : originalSegment.graph.links) {
            var copiedSource = copy_node(originalLink.source);
            var copiedTarget = copy_node(originalLink.target);

            if (originalLink instanceof LinkReference) {
                var refName = ((LinkReference)originalLink).name;
                var refOriginal = segments.find(refName);

                if (namedLines.containsKey(refName)) {
                    var recursiveLine = namedLines.get(refName);
                    var enterLine = new Line(copiedSource, recursiveLine.source);
                    var exitLine = new Line(recursiveLine.target, copiedTarget);

                    mergeEnter.put(enterLine, refName);
                    mergeExit.put(exitLine, refName);

                    afterMerge.add(() -> {
                        var enterNode = requireEquals(enterLine.source, enterLine.target);

                        for (var link : root.findIncomingLinks(enterNode)) {
                            for (var action : originalLink.beforeActions) {
                                link.afterActions.add(action);
                            }
                            link.afterActions.add(new RecursionEnter(refName));
                        }

                        var exitNode = requireEquals(exitLine.source, exitLine.target);

                        for (var link : root.findOutgoingLinks(exitNode)) {
                            for (var action : originalLink.afterActions) {
                                link.beforeActions.addTop(action);
                            }
                            link.beforeActions.addTop(new RecursionExit(refName));
                        }
                    });
                }
                else {
                    map_segment(refName, refOriginal, new Line(copiedSource, copiedTarget));
                    copy_segment(refOriginal, beforeMerge, afterMerge);
                    unmap_segment(refName, refOriginal);

                    // Distribute reference actions
                    for (var link : root.walkLinksFrom(copiedSource)) {
                        link.beforeActions.add(originalLink.beforeActions);
                    }
                    for (var link : root.walkLinksTo(copiedTarget)) {
                        link.afterActions.add(originalLink.afterActions);
                    }
                }
            }
            else if (originalLink instanceof LinkSymbol) {
                var symbol = ((LinkSymbol)originalLink).symbol;
                var link = root.createLink(copiedSource, copiedTarget, symbol);

                link.beforeActions.add(originalLink.beforeActions);
                link.afterActions.add(originalLink.afterActions);
            }
            else {
                throw new RuntimeException();
            }
        }
    }

    private void merge_nodes(Node source, Node target) {
        var result = root.merge(source, target);

        for (var entry : copiedNodes.entrySet()) {
            if (isAny(entry.getValue(), source, target)) {
                entry.setValue(result);
            }
        }

        for (var namedLine : namedLines.values()) {
            if (isAny(namedLine.source, source, target)) {
                namedLine.source = result;
            }
            if (isAny(namedLine.target, source, target)) {
                namedLine.target = result;
            }
        }

        for (var merge : mergeEnter.keySet()) {
            if (isAny(merge.source, source, target)) {
                merge.source = result;
            }
            if (isAny(merge.target, source, target)) {
                merge.target = result;
            }
        }

        for (var merge : mergeExit.keySet()) {
            if (isAny(merge.source, source, target)) {
                merge.source = result;
            }
            if (isAny(merge.target, source, target)) {
                merge.target = result;
            }
        }
    }

    private Node copy_node(Node original) {
        var copy = copiedNodes.get(original);

        if (copy == null) {
            copy = root.createNode();

            copiedNodes.put(original, copy);
        }

        return copy;
    }

    private void map_segment(String name, Segment original, Line line) {
        namedLines.put(name, line);

        for (var originalSource : original.sources) {
            if (copiedNodes.put(originalSource, line.source) != null) {
                throw new RuntimeException();
            }
        }

        for (var originalTarget : original.targets) {
            if (copiedNodes.put(originalTarget, line.target) != null) {
                throw new RuntimeException();
            }
        }
    }

    private void unmap_segment(String name, Segment original) {
        for (var originalSource : original.sources) {
            if (copiedNodes.remove(originalSource) == null) {
                throw new RuntimeException();
            }
        }

        for (var originalTarget : original.targets) {
            if (copiedNodes.remove(originalTarget) == null) {
                throw new RuntimeException();
            }
        }

        namedLines.remove(name);
    }

    public Graph getGraph() {
        return root;
    }
}
