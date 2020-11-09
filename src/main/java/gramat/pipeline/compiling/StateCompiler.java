package gramat.pipeline.compiling;

import gramat.actions.*;
import gramat.badges.BadgeSource;
import gramat.framework.Context;
import gramat.machine.Effect;
import gramat.machine.State;
import gramat.graph.*;
import gramat.symbols.Alphabet;
import gramat.util.Count;
import gramat.graph.sets.NodeSet;

import java.util.*;

import static gramat.util.DataUtils.map;

public class StateCompiler {

    public static State compile(Context ctx, Machine machine, Alphabet alphabet, BadgeSource badges) {
        try (var ignored = ctx.pushLayer("Compiling State Machine")) {
            var compiler = new StateCompiler(ctx, machine.graph, alphabet, badges);

            return compiler.compile(machine.root);
        }
    }

    private final Context ctx;
    private final Graph graph;
    private final Map<String, State> idStates;
    private final Count nextId;
    private final Alphabet symbols;
    private final BadgeSource badges;

    private StateCompiler(Context ctx, Graph graph, Alphabet symbols, BadgeSource badges) {
        this.ctx = ctx;
        this.graph = graph;
        this.idStates = new LinkedHashMap<>();
        this.nextId = new Count();
        this.symbols = symbols;
        this.badges = badges;
    }

    private State compile(Root root) {
        var initial = makeState(NodeSet.of(root.source), root.targets);
        var queue = new LinkedList<NodeSet>();
        var control = new HashSet<String>();
        var pValue = 0;
        var pTotal = 0;

        ctx.info("Converting to deterministic machine...");

        queue.add(NodeSet.of(root.source));
        pTotal++;

        while (queue.size() > 0) {
            var sources = queue.remove();
            var sourcesID = sources.computeID();

            ctx.setTotal(pValue, pTotal);
            pValue++;

            if (control.add(sourcesID)) {
                for (var symbol : symbols) {
                    for (var badge : badges) {
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

            if (nodes.anyMatch(n -> n.wild)) {
                var symbol = symbols.wild();
                var badge = badges.empty();
                state.transition.add(badge, symbol, new Effect(state));
            }

            idStates.put(id, state);
        }

        return state;
    }

}
