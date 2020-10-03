package gramat.compilers;

import gramat.actions.ActionStore;
import gramat.machine.State;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.graph.*;
import gramat.util.Count;

import java.util.*;

public class StateCompiler extends DefaultComponent {

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
                    for (var badge : gramat.badges) {
                        var links = graph.findTransitions(sources, symbol, badge);

                        if (links.size() > 0) {
                            var targets = Link.collectTargets(links);
                            var newSource = make_node(sources);
                            var newTarget = make_node(targets);
                            var before = new ActionStore();
                            var after = new ActionStore();

                            for (var link : links) {
                                before.append(link.beforeActions);
                                after.append(link.afterActions);
                            }

                            newSource.createTransition(symbol, badge, newTarget, before.toArray(), after.toArray());

                            queue.add(targets);
                        }
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
                var symbol = gramat.symbols.wild();

                // TODO what badge should it use?
                state.createTransition(symbol, gramat.badges.empty(), state, null, null);
            }

            idStates.put(id, state);
        }

        return state;
    }

}
