package gramat.compilers;

import gramat.actions.Action;
import gramat.actions.ActionStore;
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
import gramat.util.Count;

import java.util.*;

public class StateCompiler extends DefaultComponent {

    private final Graph graph;

    private final Map<String, State> idStates;
    private final Count nextId;

    public StateCompiler(Component parent, Graph graph) {
        super(parent);
        this.graph = graph;
        this.idStates = new HashMap<>();
        this.nextId = new Count();
    }

    public State compile(Segment segment) {
        var initial = makeState(segment.sources, segment.targets);
        var queue = new LinkedList<NodeSet>();
        var control = new HashSet<String>();

        queue.add(segment.sources);

        while (queue.size() > 0) {
            var sources = queue.remove();
            var sourcesID = sources.computeID();

            if (control.add(sourcesID)) {
                for (var symbol : gramat.symbols) {
                    for (var badge : gramat.badges) {
                        var links = graph.findTransitions(sources, symbol, badge);

                        if (links.size() > 0) {
                            var targets = Link.collectTargets(links);
                            var newSource = makeState(sources, segment.targets);
                            var newTarget = makeState(targets, segment.targets);
                            var before = new ActionStore();
                            var after = new ActionStore();
                            var mode = collapseMode(links);

                            Badge newBadge;
                            Action beforeAction;
                            Action afterAction;

                            if (mode == BadgeMode.NONE || badge == gramat.badges.empty()) {
                                newBadge = gramat.badges.empty();
                                beforeAction = null;
                                afterAction = null;
                            }
                            else if (mode == BadgeMode.PUSH) {
                                newBadge = gramat.badges.empty();
                                beforeAction = new RecursionEnter(badge);
                                afterAction = null;
                            }
                            else if (mode == BadgeMode.PEEK) {
                                newBadge = badge;
                                beforeAction = null;
                                afterAction = null;
                            }
                            else if (mode == BadgeMode.POP) {
                                newBadge = badge;
                                beforeAction = null;
                                afterAction = new RecursionExit(badge);
                            }
                            else {
                                throw new UnsupportedValueException(mode);
                            }

                            if (beforeAction != null) {
                                before.append(beforeAction);
                            }

                            for (var link : links) {
                                before.append(link.beforeActions);
                                after.append(link.afterActions);
                            }

                            if (afterAction != null) {
                                after.append(afterAction);
                            }

                            newSource.transition.add(newBadge, symbol, new Effect(newTarget, before.toArray(), after.toArray()));

                            queue.add(targets);
                        }
                    }
                }
            }
        }

        return initial;
    }

    private BadgeMode collapseMode(List<LinkSymbol> links) {
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

    private State makeState(NodeSet nodes, NodeSet accepted) {
        var id = nodes.computeID();
        var state = idStates.get(id);

        if (state == null) {
//            node = new Node(id);  // TODO only in debug mode
            state = new State(String.valueOf(nextId.nextString()));
            state.accepted = nodes.containsAny(accepted);

            if (nodes.toCollection().stream().anyMatch(n -> n.wild)) { // TODO improve this operation
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
