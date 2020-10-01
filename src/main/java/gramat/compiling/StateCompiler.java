package gramat.compiling;

import gramat.eval.StateFormatter;
import gramat.eval.State;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.proto.*;
import gramat.expressions.ExpressionMap;
import gramat.util.Count;

import java.util.*;

public class StateCompiler extends DefaultComponent {

    public static State compileExpression(Component parent, String name, ExpressionMap expressions) {
        var segments = new SegmentMap();
        var expressionCompiler = new ExpressionCompiler(parent);

        System.out.println("========== EXPRESSIONS");

        for (var reference : expressions.getExpressionNames()) {
            var expression = expressions.findExpression(reference);
            var segment = expressionCompiler.compile(expression);

            new NodeFormatter(System.out).write(segment);

            segments.register(reference, segment);
        }

        var resolver = new SegmentResolver(segments);
        var graph = resolver.getGraph();
        var line = resolver.resolve(name);

        System.out.println("========== RESOLVED: " + name);

        new NodeFormatter(System.out).write(graph, line);

        var stateCompiler = new StateCompiler(parent, graph);

        var state = stateCompiler.compile(graph.segment(line.source, line.target));

        System.out.println("========== STATE: " + name);

        new StateFormatter(System.out).write(state);

        return state;
    }

    private final Graph graph;

    private final Map<String, State> idStates;
    private final Map<String, NodeSet> idNodes;
    private final Count nextId;

    public StateCompiler(Component parent, Graph graph) {
        super(parent);
        this.graph = graph;
        this.idStates = new HashMap<>();
        this.idNodes = new HashMap<>();
        this.nextId = new Count();
    }

    public State compile(Segment segment) {
        var initial = make_node(segment.sources);

        make_deterministic(segment.sources);

        mark_accepted_nodes(segment.targets);

        return initial;
    }

    private void make_deterministic(NodeSet initial) {
        var queue = new LinkedList<NodeSet>();
        var control = new HashSet<String>();

        queue.add(initial);

        while (queue.size() > 0) {
            var sources = queue.remove();
            var sourcesID = sources.computeID();

            if (control.add(sourcesID)) {
                idNodes.put(sourcesID, sources);

                for (var symbol : gramat.symbols) {
                    var links = graph.findOutgoingLinks(sources, symbol);

                    if (links.size() > 0) {
                        var targets = Link.collectTargets(links);
                        var newSource = make_node(sources);
                        var newTarget = make_node(targets);

                        var transition = newSource.addTransition(symbol, newTarget);

                        for (var link : links) {
                            for (var action : link.beforeActions) {
                                transition.addBefore(action);
                            }

                            for (var action : link.afterActions) {
                                transition.addAfter(action);
                            }
                        }

                        queue.add(targets);
                    }
                }
            }
        }
    }

    private void mark_accepted_nodes(NodeSet targets) {
        for (var idState : idStates.entrySet()) {
            var id = idState.getKey();
            var state = idState.getValue();
            var nodes = idNodes.get(id);

            if (nodes.containsAny(targets)) {
                state.markAccepted();
            }
        }
    }

    private State make_node(NodeSet nodes) {
        var id = nodes.computeID();
        var state = idStates.get(id);

        if (state == null) {
//            node = new Node(id);  // TODO only in debug mode
            state = new State(String.valueOf(nextId.nextString()));

            if (nodes.toCollection().stream().anyMatch(n -> n.wild)) { // TODO improve this operation
                var symbol = gramat.symbols.makeWild();

                state.addTransition(symbol, state);
            }

            idStates.put(id, state);
        }

        return state;
    }

}
