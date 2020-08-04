package gramat.engine.indet;

import gramat.GramatException;
import gramat.engine.AmCode;
import gramat.engine.nodet.*;
import gramat.engine.symbols.Symbol;
import gramat.engine.deter.DState;
import gramat.engine.checks.Check;

import java.util.*;

public class IMachine {

    public final IState initial;

    private final List<IState> states;
    private final List<ITransition> transitions;

    public IMachine(NLanguage lang, NMachine source) {
        this.states = new ArrayList<>();
        this.transitions = new ArrayList<>();
        this.initial = make_deterministic(lang, source);
    }

    private IState make_deterministic(NLanguage lang, NMachine source) {
        var queue = new LinkedList<NStateList>();
        var control = new HashSet<String>();
        var initialClosure = source.initial.getEmptyClosure();

        var checksAndNull = new ArrayList<>(lang.checks.getItems());
        checksAndNull.add(null);  // TODO this is not beautiful

        queue.add(initialClosure);

        do {
            var oldSources = queue.remove();

            if (control.add(oldSources.computeID())) {
                for (var symbol : lang.symbols) {
                    for (var check : checksAndNull) {
                        var oldTransitions = lang.findTransitionsFrom(oldSources, symbol, check);

                        if (oldTransitions.size() > 0) {
                            // get empty closure of all targets
                            var oldTargets = oldTransitions.collectTargets().getEmptyClosure();

                            if (oldTargets.size() > 0) {
                                var newSource = getOrCreateState(oldSources);
                                var newTarget = getOrCreateState(oldTargets);

                                createTransition(newSource, newTarget, symbol, check);

                                queue.add(oldTargets);
                            }
                        }
                    }
                }
            }
        } while (queue.size() > 0);

        var initial = getState(initialClosure);

        if (initial == null) {
            throw new GramatException("cannot find initial state");
        }

        // mark accepted states
        for (var state : states) {
            if (state.origin.contains(source.accepted)) {
                state.accepted = true;
            }
        }

        return initial;
    }

    public DState compile() {
        System.out.println("I-DFA >>>>>>>>>>");
        AmCode.write(System.out, this);
        System.out.println("<<<<<<<<<< I-DFA");
        return null;
    }

    private void createTransition(IState source, IState target, Symbol symbol, Check check) {
        var transition = new ITransition(source, target, symbol, check);

        transitions.add(transition);
    }

    public IState getOrCreateState(NStateList items) {
        var state = getState(items);

        if (state != null) {
            return state;
        }

        return createState(items);
    }

    private IState createState(NStateList items) {
        if (items.isEmpty()) {
            throw new GramatException("unexpected empty state list");
        }

        var state = new IState(items);

        states.add(state);

        return state;
    }

    public IState getState(NStateList nStates) {
        var id = nStates.computeID();

        for (var state : this.states) {
            if (Objects.equals(state.computeID(), id)) {
                return state;
            }
        }

        return null;
    }

    public List<ITransition> findTransitionsBySource(IState source) {
        var result = new ArrayList<ITransition>();
        for (var trn : transitions) {
            if (trn.source == source) {
                result.add(trn);
            }
        }
        return result;
    }

}
