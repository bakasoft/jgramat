//package gramat.model;
//
//import gramat.actions.Action;
//import gramat.framework.Component;
//import gramat.framework.DefaultComponent;
//import gramat.proto.*;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.function.Function;
//
//public class SectionCompiler extends DefaultComponent {
//
//    private final SegmentMap segmentMap;
//    private final SectionGrammar sectionGrammar;
//
//    public SectionCompiler(Component parent, SegmentMap segmentMap, SectionGrammar sectionGrammar) {
//        super(parent);
//        this.segmentMap = segmentMap;
//        this.sectionGrammar = sectionGrammar;
//    }
//
//    public Section compile(String name) {
//        var graph = segmentMap.find(name);
//
//        return make_graph(name, graph);
//    }
//
//    private Section make_graph(String name, Graph graph) {
//        var section = sectionGrammar.searchSection(name);
//
//        if (section != null) {
//            return section;
//        }
//
//        section = sectionGrammar.createSection(name);
//
//        var promises = new ArrayList<Runnable>();
//
//        deep_copy(graph, section, promises);
//
//        for (var promise : promises) {
//            promise.run();
//        }
//
//        return section;
//    }
//
//    private void deep_copy(Graph graph, Section section, List<Runnable> promises) {
//        // Local node mapping only for the segment
//        var vertexNodes = new HashMap<Vertex, Node>();
//        var nodeMaker = (Function<Vertex, Node>) vertex ->
//                vertexNodes.computeIfAbsent(vertex, v -> sectionGrammar.createNode());
//
//        // Initialize vertex mapping with well-known nodes
//        for (var source : graph.entryPoint.sources) {
//            vertexNodes.put(source, section.initial);
//        }
//        for (var target : graph.entryPoint.targets) {
//            vertexNodes.put(target, section.accepted);
//        }
//
//        for (var edge : graph.edges) {
//            var nodeSource = nodeMaker.apply(edge.source);
//            var nodeTarget = nodeMaker.apply(edge.target);
//            if (edge instanceof EdgeSymbol) {
//                var edgeSymbol = (EdgeSymbol)edge;
//                var link = sectionGrammar.createLink(nodeSource, nodeTarget, edgeSymbol.symbol);
//
//                link.beforeActions.addAll(edgeSymbol.beforeActions);
//                link.afterActions.addAll(edgeSymbol.afterActions);
//            }
//            else if (edge instanceof EdgeReference) {
//                var edgeReference = (EdgeReference)edge;
//                var refGraph = segmentMap.find(edgeReference.name);
//
//                if (segmentMap.isFlat(refGraph)) {
//                    var refSection = new Section(nodeSource, nodeTarget);
//
//                    // Embed segment and distribute actions right here
//                    deep_copy(refGraph, refSection, promises);
//
//                    // Actions distribution is deferred because the graph might not be complete at this point
//                    promises.add(() -> {
//                        apply_actions_forward(nodeSource, edgeReference.beforeActions, nodeTarget);
//                        apply_actions_backward(nodeSource, edgeReference.afterActions, nodeTarget);
//                    });
//                }
//                else {
//                    var refSection = make_graph(edgeReference.name, refGraph);
//
//                    // TODO link using symbols instead of empty
//
//                    // Create recursion instead of reference
//                    sectionGrammar.createLink(nodeSource, refSection.initial);
//                    sectionGrammar.createLink(refSection.accepted, nodeTarget);
//
//                    // Actions distribution is deferred because the graph might not be complete at this point
//                    promises.add(() -> {
//                        apply_actions_forward(nodeSource, edgeReference.beforeActions, nodeTarget);
//                        apply_actions_backward(nodeSource, edgeReference.afterActions, nodeTarget);
//                        // TODO add recursion control
//                    });
//                }
//            }
//            else {
//                sectionGrammar.createLink(nodeSource, nodeTarget);
//            }
//        }
//    }
//
//    private void apply_actions_forward(Node source, List<Action> actions, Node target) {
//        if (actions.size() > 0) {
//            var count = 0;
//
//            for (var transition : sectionGrammar.findForwardSymbols(source)) {
//                if (sectionGrammar.areForwardLinked(transition.target, target)) {
//                    transition.beforeActions.addAll(actions);
//                    count++;
//                }
//            }
//
//            if (count == 0) {
//                throw new RuntimeException();
//            }
//        }
//    }
//
//    private void apply_actions_backward(Node source, List<Action> actions, Node target) {
//        if (actions.size() > 0) {
//            var count = 0;
//
//            for (var transition : sectionGrammar.findBackwardSymbols(target)) {
//                if (sectionGrammar.areBackwardLinked(transition.source, source)) {
//                    transition.afterActions.addAll(actions);
//                    count++;
//                }
//            }
//
//            if (count == 0) {
//                throw new RuntimeException();
//            }
//        }
//    }
//
//}
