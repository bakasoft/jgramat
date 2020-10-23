package gramat.pipeline;

import gramat.actions.*;
import gramat.framework.Progress;
import gramat.machine.Effect;
import gramat.machine.State;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.graph.*;
import gramat.util.Count;
import gramat.graph.sets.NodeSet;

import java.util.*;

import static gramat.util.DataUtils.map;

public class StateCompiler extends DefaultComponent {

    public static State compile(Component parent, Machine machine) {
        try (var progress = new Progress("Compiling State Machine")) {
            var compiler = new StateCompiler(parent, progress, machine.graph);

            return compiler.compile(machine.root);
        }
    }

    private final Progress progress;
    private final Graph graph;
    private final Map<String, State> idStates;
    private final Count nextId;

    private StateCompiler(Component parent, Progress progress, Graph graph) {
        super(parent);
        this.progress = progress;
        this.graph = graph;
        this.idStates = new HashMap<>();
        this.nextId = new Count();
    }

    private State compile(Root root) {
        var initial = makeState(NodeSet.of(root.source), root.targets);
        var queue = new LinkedList<NodeSet>();
        var control = new HashSet<String>();
        var pValue = 0;
        var pTotal = 0;

        progress.log("Converting to deterministic machine...");

        queue.add(NodeSet.of(root.source));
        pTotal++;

        while (queue.size() > 0) {
            var sources = queue.remove();
            var sourcesID = sources.computeID();

            progress.log(pValue, pTotal, "Processing %s...", sourcesID);
            pValue++;

            if (control.add(sourcesID)) {
                for (var symbol : gramat.symbols) {
                    for (var badge : gramat.badges) {
                        var links = graph.findTransitions(sources, symbol, badge);

                        if (!links.isEmpty()) {
                            var targets = Link.collectTargets(links);
                            var newSource = makeState(sources, root.targets);
                            var newTarget = makeState(targets, root.targets);
                            var event = Event.collect(map(links, link -> link.event));

                            newSource.transition.add(badge, symbol, new Effect(newTarget, event.before.toArray(), event.after.toArray()));

                            queue.add(targets);
                            pTotal++;
                        }
                    }
                }
            }
        }

        return initial;
    }

    private State makeState(NodeSet nodes, NodeSet accepted) {
        var id =  nodes.computeID();
        var state = idStates.get(id);

        if (state == null) {
//            node = new Node(id);  // TODO only in debug mode
            state = new State(String.valueOf(nextId.nextString()));
            state.accepted = nodes.containsAny(accepted);

            if (nodes.anyMatch(n -> n.wild)) { // TODO improve this operation
                var symbol = gramat.symbols.wild();
                var badge = gramat.badges.empty();
                // TODO what badge should it use?
                state.transition.add(badge, symbol, new Effect(state));
            }

            idStates.put(id, state);
        }

        return state;
    }

}
