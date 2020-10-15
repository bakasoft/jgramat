package gramat.pipeline;

import gramat.actions.Action;
import gramat.actions.Event;
import gramat.actions.RecursionEnter;
import gramat.actions.RecursionExit;
import gramat.badges.Badge;
import gramat.badges.BadgeMode;
import gramat.exceptions.UnsupportedValueException;
import gramat.machine.Effect;
import gramat.machine.State;
import gramat.framework.Component;
import gramat.framework.DefaultComponent;
import gramat.graph.*;
import gramat.util.Chain;
import gramat.util.Count;

import java.util.*;

public class StateCompiler extends DefaultComponent {

    public static State compile(Component parent, Machine machine) {
        var compiler = new StateCompiler(parent, machine.graph);

        return compiler.compile(machine.root);
    }

    private final Graph graph;

    private final Map<String, State> idStates;
    private final Count nextId;

    private StateCompiler(Component parent, Graph graph) {
        super(parent);
        this.graph = graph;
        this.idStates = new HashMap<>();
        this.nextId = new Count();
    }

    private State compile(Root root) {
        var initial = makeState(Chain.of(root.source), root.targets);
        var queue = new LinkedList<Chain<Node>>();
        var control = new HashSet<String>();

        queue.add(Chain.of(root.source));

        while (queue.size() > 0) {
            var sources = queue.remove();
            var sourcesID = Node.computeID(sources);

            if (control.add(sourcesID)) {
                for (var symbol : gramat.symbols) {
                    for (var badge : gramat.badges) {
                        var links = graph.findTransitions(sources, symbol, badge);

                        if (!links.isEmpty()) {
                            var targets = Link.collectTargets(links);
                            var newSource = makeState(sources, root.targets);
                            var newTarget = makeState(targets, root.targets);
                            var mode = collapseMode(links);

                            Badge newBadge;

                            var event = Event.of();

                            for (var link : links) {
                                event = Event.of(link.getEvent(), event);
                            }

                            if (mode == BadgeMode.NONE || badge == gramat.badges.empty()) {
                                newBadge = gramat.badges.empty();
                            }
                            else if (mode == BadgeMode.PUSH) {
                                newBadge = gramat.badges.empty();
                                event = Event.of(new RecursionEnter(badge), event, null);
                            }
                            else if (mode == BadgeMode.PEEK) {
                                newBadge = badge;
                            }
                            else if (mode == BadgeMode.POP) {
                                newBadge = badge;
                                event = Event.of(null, event, new RecursionExit(badge));
                            }
                            else {
                                throw new UnsupportedValueException(mode);
                            }

                            newSource.transition.add(newBadge, symbol, new Effect(newTarget, event));

                            queue.add(targets);
                        }
                    }
                }
            }
        }

        return initial;
    }

    private BadgeMode collapseMode(List<Link> links) {
        BadgeMode mode = null;

        for (var link : links) {
            if (mode == null) {
                mode = link.mode;
            }
            else if (mode != link.mode) {
                throw new RuntimeException("ambiguous mode: " + mode + "/" + link.mode);
            }
        }

        return mode;
    }

    private State makeState(Chain<Node> nodes, Chain<Node> accepted) {
        var id = Node.computeID(nodes);
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
