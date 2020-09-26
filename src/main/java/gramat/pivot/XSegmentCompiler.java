package gramat.pivot;

import gramat.eval.Transition;
import gramat.eval.State;
import gramat.eval.StateTransition;
import gramat.pivot.data.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class XSegmentCompiler {

    private final XLanguage lang;
    private final Map<String, State> idNodes;
    private final Map<String, Set<XState>> idClosures;
    private final List<StateTransition> stateTransitions;
    private final Map<State, Set<XState>> nodeClosures;
    private final AtomicInteger nextId;

    public XSegmentCompiler(XLanguage lang) {
        this.lang = lang;
        this.idNodes = new HashMap<>();
        this.idClosures = new HashMap<>();
        this.nodeClosures = new HashMap<>();
        this.stateTransitions = new ArrayList<>();
        this.nextId = new AtomicInteger(0);
    }

    public State compile(XState initial, XState accepted, List<XActionBlock> blocks) {
        var allStates = lang.listStatesFrom(initial);
        var initialClosure = initial.computeEmptyClosure();

        make_deterministic(initialClosure);

        distribute_actions(blocks, allStates);

        mark_accepted_nodes(accepted);

        return find_initial_node(initialClosure);
    }

    private void make_deterministic(Set<XState> initialClosure) {
        var queue = new LinkedList<Set<XState>>();
        var control = new HashSet<String>();

        queue.add(initialClosure);

        // process loop
        while (queue.size() > 0) {
            var closure = queue.remove();
            var closureID = XState.computeID(closure);

            if (control.add(closureID)) {
                idClosures.put(closureID, closure);

                for (var symbol : lang.symbols) {
                    var transitions = lang.findTransitionsFrom(closure, symbol);

                    if (transitions.size() > 0) {
                        // get empty closure of all targets
                        var targets = lang.computeEmptyClosure(
                                XTransition.collectTargets(transitions)
                        );

                        if (targets.size() > 0) {
                            var newSource = make_node(closure);
                            var newTarget = make_node(targets);

                            nodeClosures.put(newSource, closure);
                            nodeClosures.put(newTarget, targets);

                            var link = newSource.addTransition(symbol, newTarget);

                            for (var transition : transitions) {
                                var data = (TDSymbol)transition.data;

                                for (var action : data.preActions) {
                                    link.addBefore(action);
                                }

                                for (var action : data.postActions) {
                                    link.addAfter(action);
                                }
                            }

                            stateTransitions.add(new StateTransition(newSource, link));

                            queue.add(targets);
                        }
                    }
                }
            }
        }
    }

    private void distribute_actions(List<XActionBlock> blocks, Set<XState> allStates) {
        for (var block : blocks) {
            if (allStates.contains(block.initial) && allStates.contains(block.accepted)) {
                var forwardTrns = lang.computeSymbolClosure(block.initial);
                var backwardTrns = lang.computeSymbolInverseClosure(block.accepted);
                var forwardSources = XTransition.collectSources(forwardTrns);
                var backwardTargets = XTransition.collectTargets(backwardTrns);

                for (var forwardTrn : forwardTrns) {
                    if (lang.areForwardLinked(forwardTrn.target, backwardTargets)) {
                        for (var link : find_links_for(forwardTrn)) {
                            link.addBefore(block.before);
                        }
                    }
                }

                for (var backwardTrn : backwardTrns) {
                    if (lang.areBackwardLinked(backwardTrn.source, forwardSources)) {
                        for (var link : find_links_for(backwardTrn)) {
                            link.addAfter(block.after);
                        }
                    }
                }
            }
        }
    }

    private List<Transition> find_links_for(XTransition transition) {
        var symbol = transition.getSymbol();
        var links = new ArrayList<Transition>();

        for (var nodes : find_nodes_for(transition.source)) {
            for (var link : nodes) {
                if (symbol == link.getSymbol() && is_node_for(link.getTarget(), transition.target)) {
                    links.add(link);
                }
            }
        }

        if (links.isEmpty()) {
            throw new RuntimeException();
        }

        return links;
    }

    private boolean is_node_for(State node, XState state) {
        for (var entry : idNodes.entrySet()) {
            if (entry.getValue() == node) {
                var id = entry.getKey();
                var closure = idClosures.get(id);

                if (closure.contains(state)) {
                    return true;
                }
            }
        }

        return false;
    }

    private List<State> find_nodes_for(XState state) {
        var nodes = new ArrayList<State>();

        for (var entry : idClosures.entrySet()) {
            var closure = entry.getValue();
            if (closure.contains(state)) {
                var id = entry.getKey();
                var node = idNodes.get(id);

                nodes.add(node);
            }
        }

        if (nodes.isEmpty()) {
            throw new RuntimeException("node not found for state: " + state.id);
        }

        return nodes;
    }

    private void mark_accepted_nodes(XState accepted) {
        for (var idNode : idNodes.entrySet()) {
            var id = idNode.getKey();
            var node = idNode.getValue();
            var closure = idClosures.get(id);

            if (closure.contains(accepted)) {
                node.markAccepted();
            }
        }
    }

    private State find_initial_node(Set<XState> closure) {
        var closureID = XState.computeID(closure);
        var node = idNodes.get(closureID);

        if (node == null) {
            throw new RuntimeException("cannot find initial node");
        }

        return node;
    }

    private State make_node(Set<XState> states) {
        var id = XState.computeID(states);
        var node = idNodes.get(id);

        if (node == null) {
//            node = new Node(id);  // TODO only in debug mode
            node = new State(String.valueOf(nextId.getAndIncrement()));

            if (states.stream().anyMatch(state -> state.wild)) {
                var symbol = lang.symbols.makeWild();

                node.addTransition(symbol, node);
            }

            idNodes.put(id, node);
        }

        return node;
    }
}
