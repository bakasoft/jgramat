package gramat.eval;

import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.model.*;
import gramat.proto.GraphCompiler;
import gramat.proto.GraphFormatter;
import gramat.proto.GraphGrammar;
import gramat.source.ExpressionGrammar;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class StateCompiler extends DefaultComponent {

    public static State compileExpression(Component parent, String name, ExpressionGrammar expressionGrammar) {
        var segmentGrammar = new GraphGrammar();
        var segmentCompiler = new GraphCompiler(parent, expressionGrammar, segmentGrammar);

        System.out.println("========== SEGMENTS");

        for (var reference : expressionGrammar.getExpressionNames()) {
            var segment = segmentCompiler.compile(reference);

            new GraphFormatter(System.out).write(segment);
        }

        var sectionGrammar = new SectionGrammar();
        var sectionCompiler = new SectionCompiler(parent, segmentGrammar, sectionGrammar);

        for (var reference : expressionGrammar.getExpressionNames()) {
            sectionCompiler.compile(reference);
        }

        var section = sectionCompiler.compile(name);

        System.out.println("========== SECTION");

        new SectionFormatter(sectionGrammar, System.out).write(section);

        var stateCompiler = new StateCompiler(parent, sectionGrammar);

        return stateCompiler.compile(section);
    }

    private final SectionGrammar sectionGrammar;

    private final Map<String, State> idStates;
    private final Map<String, Set<Node>> idNodes;
    private final List<StateTransition> stateTransitions;
    private final Map<State, Set<Node>> nodeClosures;
    private final AtomicInteger nextId;

    public StateCompiler(Component parent, SectionGrammar sectionGrammar) {
        super(parent);
        this.sectionGrammar = sectionGrammar;
        this.idStates = new HashMap<>();
        this.idNodes = new HashMap<>();
        this.nodeClosures = new HashMap<>();
        this.stateTransitions = new ArrayList<>();
        this.nextId = new AtomicInteger(0);
    }

    public State compile(Section section) {
        var initialClosure = sectionGrammar.computeEmptyClosure(section.initial);

        make_deterministic(initialClosure);

        mark_accepted_nodes(section.accepted);

        return find_initial_node(initialClosure);
    }

    private void make_deterministic(Set<Node> initialClosure) {
        var queue = new LinkedList<Set<Node>>();
        var control = new HashSet<String>();

        queue.add(initialClosure);

        // process loop
        while (queue.size() > 0) {
            var closure = queue.remove();
            var closureID = Node.computeID(closure);

            if (control.add(closureID)) {
                idNodes.put(closureID, closure);

                for (var symbol : gramat.symbols) {
                    var linkSymbols = sectionGrammar.findLinksFrom(closure, symbol);

                    if (linkSymbols.size() > 0) {
                        // get empty closure of all targets
                        var targets = sectionGrammar.computeEmptyClosure(
                                Link.collectTargets(linkSymbols)
                        );

                        if (targets.size() > 0) {
                            var newSource = make_node(closure);
                            var newTarget = make_node(targets);

                            nodeClosures.put(newSource, closure);
                            nodeClosures.put(newTarget, targets);

                            var transition = newSource.addTransition(symbol, newTarget);

                            for (var linkSymbol : linkSymbols) {
                                for (var action : linkSymbol.beforeActions) {
                                    transition.addBefore(action);
                                }

                                for (var action : linkSymbol.afterActions) {
                                    transition.addAfter(action);
                                }
                            }

                            stateTransitions.add(new StateTransition(newSource, transition));

                            queue.add(targets);
                        }
                    }
                }
            }
        }
    }

    private void mark_accepted_nodes(Node accepted) {
        for (var idState : idStates.entrySet()) {
            var id = idState.getKey();
            var state = idState.getValue();
            var nodes = idNodes.get(id);

            if (nodes.contains(accepted)) {
                state.markAccepted();
            }
        }
    }

    private State find_initial_node(Set<Node> closure) {
        var closureID = Node.computeID(closure);
        var node = idStates.get(closureID);

        if (node == null) {
            throw new RuntimeException("cannot find initial node");
        }

        return node;
    }

    private State make_node(Set<Node> nodes) {
        var id = Node.computeID(nodes);
        var state = idStates.get(id);

        if (state == null) {
//            node = new Node(id);  // TODO only in debug mode
            state = new State(String.valueOf(nextId.getAndIncrement()));

            if (nodes.stream().anyMatch(n -> n.wild)) {
                var symbol = gramat.symbols.makeWild();

                state.addTransition(symbol, state);
            }

            idStates.put(id, state);
        }

        return state;
    }

}
