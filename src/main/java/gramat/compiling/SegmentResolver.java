package gramat.compiling;

import gramat.actions.RecursionHalt;
import gramat.actions.RecursionKeep;
import gramat.proto.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static gramat.util.Validations.isAny;
import static gramat.util.Validations.requireEquals;

public class SegmentResolver {

    private final Graph root;
    private final SegmentMap segments;

    private final Map<String, Line> namedLines;
    private final Map<Vertex, Vertex> copiedVertex;
    private final Map<Line, String> mergeEnter;
    private final Map<Line, String> mergeExit;

    public SegmentResolver(SegmentMap segments) {
        this.segments = segments;
        this.root = new Graph();
        this.namedLines = new HashMap<>();
        this.copiedVertex = new HashMap<>();
        this.mergeEnter = new LinkedHashMap<>();
        this.mergeExit = new LinkedHashMap<>();
    }

    public Line resolve(String name) {
        var original = segments.find(name);

        return resolve(name, original);
    }

    public Line resolve(String name, Segment original) {
        var step1 = new ArrayList<Runnable>();
        var step2 = new ArrayList<Runnable>();
        var line = resolve_segment(name, original, step1, step2);

        for (var step : step1) {
            step.run();
        }

        for (var step : step2) {
            step.run();
        }

        for (var merge : mergeEnter.keySet()) {
            merge_vertices(merge.source, merge.target);
        }

        for (var merge : mergeExit.keySet()) {
            merge_vertices(merge.source, merge.target);
        }

        for (var entry : mergeEnter.entrySet()) {
            var merge = entry.getKey();
            var token = entry.getValue();
            var vertex = requireEquals(merge.source, merge.target);

            for (var edge : root.findEdgesFrom(vertex)) {
                edge.beforeActions.add(new RecursionKeep(0, token));
            }
        }

        for (var entry : mergeExit.entrySet()) {
            var merge = entry.getKey();
            var token = entry.getValue();
            var vertex = requireEquals(merge.source, merge.target);

            for (var edge : root.findEdgesFrom(vertex)) {
                edge.afterActions.add(new RecursionHalt(0, token));
            }
        }

        return line;
    }

    private Line resolve_segment(String name, Segment original, List<Runnable> step1, List<Runnable> step2) {
        var line = namedLines.get(name);

        if (line != null) {
            return line;
        }

        line = root.createLine();

        map_segment(name, original, line);

        copy_segment(original, step1, step2);

        unmap_segment(name, original);

        return line;
    }

    private void copy_segment(Segment originalSegment, List<Runnable> step1, List<Runnable> step2) {
        for (var originalEdge : originalSegment.graph.edges) {
            var copiedSource = copy_vertex(originalEdge.source);
            var copiedTarget = copy_vertex(originalEdge.target);

            if (originalEdge instanceof EdgeReference) {
                var refName = ((EdgeReference)originalEdge).name;
                var refOriginal = segments.find(refName);

                if (namedLines.containsKey(refName)) {
//                    System.out.println("RECURSIVE " + refName);
                    var recursiveLine = namedLines.get(refName);

                    mergeEnter.put(new Line(copiedSource, recursiveLine.source), refName);
                    mergeExit.put(new Line(recursiveLine.target, copiedTarget), refName);
                }
                else {
                    map_segment(refName, refOriginal, new Line(copiedSource, copiedTarget));
                    copy_segment(refOriginal, step1, step2);

//                    for (var edge : root.listEdgesFrom(copiedSource)) {
//                        System.out.println("ENTER " + edge.source.id + " -> " + edge.target.id + " " + refName);
//                    }
//                    for (var edge : root.listEdgesTo(copiedTarget)) {
//                        System.out.println("EXIT " + edge.source.id + " -> " + edge.target.id + " " + refName);
//                    }

                    unmap_segment(refName, refOriginal);
                }
            }
            else if (originalEdge instanceof EdgeSymbol) {
                var symbol = ((EdgeSymbol)originalEdge).symbol;

                root.createEdge(copiedSource, copiedTarget, symbol);
            }
            else {
                throw new RuntimeException();
            }
        }
    }

    private void merge_vertices(Vertex source, Vertex target) {
        var result = root.merge(source, target);

        for (var entry : copiedVertex.entrySet()) {
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

    private Vertex copy_vertex(Vertex original) {
        var copy = copiedVertex.get(original);

        if (copy == null) {
            copy = root.createVertex();

            copiedVertex.put(original, copy);
        }

        return copy;
    }

    private void map_segment(String name, Segment original, Line line) {
        namedLines.put(name, line);

        for (var originalSource : original.sources) {
            if (copiedVertex.put(originalSource, line.source) != null) {
                throw new RuntimeException();
            }
        }

        for (var originalTarget : original.targets) {
            if (copiedVertex.put(originalTarget, line.target) != null) {
                throw new RuntimeException();
            }
        }
    }

    private void unmap_segment(String name, Segment original) {
        for (var originalSource : original.sources) {
            if (copiedVertex.remove(originalSource) == null) {
                throw new RuntimeException();
            }
        }

        for (var originalTarget : original.targets) {
            if (copiedVertex.remove(originalTarget) == null) {
                throw new RuntimeException();
            }
        }

        namedLines.remove(name);
    }

    public Graph getGraph() {
        return root;
    }
}
