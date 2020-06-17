package gramat.automata.ndfa;

import gramat.eval.Action;
import gramat.expressions.flat.BeginSource;

import java.util.*;
import java.util.stream.Collectors;

public class NLanguage {

    private final List<NState> states;
    public final List<NTransition> transitions;  // TODO make private
    public final SymbolSet symbols;

    private final Stack<NGroup> groupStack;

    private int nextStateID;

    public NLanguage() {
        states = new ArrayList<>();
        transitions = new ArrayList<>();
        symbols = new SymbolSet();
        groupStack = new Stack<>();
        nextStateID = 1;
    }

    public void openGroup() {
        System.out.println("OPEN GROUP");
        groupStack.push(new NGroup());
    }

    public NGroup closeGroup() {
        System.out.println("CLOSE GROUP");
        return groupStack.pop();
    }

    public NState state() {
        var state = new NState(this, nextStateID);

        nextStateID++;

        states.add(state);

        for (var group : groupStack) {
            group.states.add(state);
        }

        return state;
    }

    public void transition(NStateSet sources, NState target, Symbol symbol) {
        transition(sources, NStateSet.of(target), symbol);
    }

    public void transition(NState source, NStateSet targets, Symbol symbol) {
        transition(NStateSet.of(source), targets, symbol);
    }

    public void transition(NStateSet sources, NStateSet targets, Symbol symbol) {
        if (sources.isEmpty()) {
            throw new RuntimeException("Missing source states");
        }
        else if (targets.isEmpty()) {
            throw new RuntimeException("Missing target states");
        }

        for (var source : sources) {
            for (var target : targets) {
                transition(source, target, symbol);
            }
        }
    }

    public void transition(NState source, NState target, Symbol symbol) {
        // avoid duplicate transitions
        var transition = findTransition(source, target, symbol);

        if (transition == null) {
            transition = new NTransition(source, target, symbol);

            transitions.add(transition);
        }

        System.out.println("NEW " + transition);

        for (var group : groupStack) {
            if (!group.transitions.contains(transition)) {
                group.transitions.add(transition);
            }
        }
    }

    private NTransition findTransition(NState source, NState target, Symbol symbol) {
        for (var trn : transitions) {
            if (trn.source == source && trn.target == target && trn.symbol == symbol) {
                return trn;
            }
        }
        return null;
    }

    public List<NTransition> findTransitionsBySource(NState source) {
        return transitions.stream()
                .filter(t -> t.source == source)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<NTransition> findTransitionsByTarget(NState target) {
        return transitions.stream()
                .filter(t -> t.target == target)
                .collect(Collectors.toUnmodifiableList());
    }

    public NStateSet computeNullClosure(NState state) {
        var closure = new NStateSet();
        var queue = new LinkedList<NState>();

        queue.add(state);

        do {
            var source = queue.remove();

            if (closure.add(source)) {
                for (var trn : transitions) {
                    if (trn.source == source && trn.symbol == null) {
                        queue.add(trn.target);
                    }
                }
            }
        } while (queue.size() > 0);

        return closure;
    }

    public NStateSet computeInverseNullClosure(NState state) {
        var result = new NStateSet();
        var queue = new LinkedList<NState>();
        var control = new HashSet<NState>();

        queue.add(state);

        result.add(state);

        do {
            var target = queue.remove();

            if (control.add(target)) {
                for (var trn : transitions) {
                    if (trn.target == target && trn.symbol == null) {
                        result.add(trn.source);

                        queue.add(trn.source);
                    }
                }
            }
        }
        while(queue.size() > 0);

        return result;
    }
}
