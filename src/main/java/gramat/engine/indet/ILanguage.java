package gramat.engine.indet;

import gramat.GramatException;
import gramat.engine.AmCode;
import gramat.engine.deter.DTransition;
import gramat.engine.nodet.*;
import gramat.engine.symbols.Symbol;
import gramat.engine.deter.DState;
import gramat.engine.symbols.SymbolSource;

import java.util.*;

public class ILanguage {

    private final List<IState> states;
    private final List<ITransition> transitions;

    public ILanguage() {
        this.states = new ArrayList<>();
        this.transitions = new ArrayList<>();
    }

    public IState convertToDFA(NLanguage lang, SymbolSource symbols, NMachine source) {
        // convert NDFA to DFA
        var queue = new LinkedList<NStateList>();
        var control = new HashSet<String>();
        var initialClosure = source.initial.getEmptyClosure();

        queue.add(initialClosure);

        do {
            var oldSources = queue.remove();

            if (control.add(oldSources.computeID())) {
                for (var symbol : symbols) {
                    var oldTransitions = lang.findTransitionsFrom(oldSources, symbol);

                    if (oldTransitions.size() > 0) {
                        // get empty closure of all targets
                        var oldTargets = oldTransitions.collectTargets().getEmptyClosure();

                        if (oldTargets.size() > 0) {
                            var newSource = mergeState(oldSources);
                            var newTarget = mergeState(oldTargets);

                            createTransition(newSource, newTarget, symbol);

                            queue.add(oldTargets);
                        }
                    }
                }
            }
        } while (queue.size() > 0);

        // retrieve initial state
        var initial = lookupStateByOrigin(initialClosure);

        if (initial == null) {
            throw new GramatException("cannot find initial state");
        }

        // mark accepted states
        for (var state : states) {
            if (state.origin.contains(source.accepted)) {
                state.accepted = true;
            }
        }

        // distribute actions
        for (var iTran : this.transitions) {
            for (var nTran : lang.transitions) {
                // skip not matching symbols (null symbols actually matches)
                if (nTran.symbol != null && !Objects.equals(iTran.symbol, nTran.symbol)) {
                    continue;
                }
                // skip not matching directions
                else if (!iTran.source.origin.contains(nTran.source) || !iTran.target.origin.contains(nTran.target)) {
                    continue;
                }

                // apply actions
                iTran.actions.addAll(nTran.actions);
            }
        }

        System.out.println("I-DFA >>>>>>>>>>");
        AmCode.write(System.out, this, initial);
        System.out.println("<<<<<<<<<< I-DFA");

        return initial;
    }

    public DState compile(IState initial) {
        var stateMap = new HashMap<IState, DState>();

        return make_deter(initial, stateMap);
    }

    private DState make_deter(IState iState, Map<IState, DState> map) {
        var dState = map.get(iState);

        if (dState != null) {
            return dState;
        }

        // map state
        dState = new DState(map.size(), iState.accepted);

        map.put(iState, dState);

        // map transitions
        var iTrans = findTransitionsBySource(iState);

        if (iTrans.size() > 0) {
            var dTrans = new DTransition[iTrans.size()];

            for (var i = 0; i < iTrans.size(); i++) {
                var iTran = iTrans.get(i);
                var dTarget = make_deter(iTran.target, map);

                dTrans[i] = new DTransition(iTran.symbol, dTarget, iTran.actions.toArray());
            }

            dState.transitions = dTrans;
        }

        return dState;
    }

    public void createTransition(IState source, IState target, Symbol symbol) {
        var transition = new ITransition(source, target, symbol);

        transitions.add(transition);
    }

    public IState mergeState(NStateList items) {
        var state = lookupStateByOrigin(items);
        if (state != null) {
            return state;
        }
        return createState(items);
    }

    private IState createState(NStateList origin) {
        if (origin.isEmpty()) {
            throw new GramatException("unexpected empty state list");
        }
        return createState(
                origin.computeID(),
                origin);
    }

    public IState createState(String id) {
        return createState(id, new NStateList());
    }

    public IState createState(String id, boolean accepted) {
        var state = createState(id);
        state.accepted = accepted;
        return state;
    }

    public IState createState(String id, NStateList origin) {
        var state = new IState(id, origin);

        if (lookupStateByID(id) != null) {
            throw new RuntimeException("ID already exists: " + id);
        }

        states.add(state);

        return state;
    }

    public IState lookupStateByID(String id) {
        for (var state : states) {
            if (Objects.equals(state.id, id)) {
                return state;
            }
        }
        return null;
    }

    public IState lookupStateByOrigin(NStateList origin) {
        return lookupStateByID(origin.computeID());
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
