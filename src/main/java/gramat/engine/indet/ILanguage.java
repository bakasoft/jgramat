package gramat.engine.indet;

import gramat.GramatException;
import gramat.engine.AmCode;
import gramat.engine.deter.DTransition;
import gramat.engine.nodet.*;
import gramat.engine.symbols.Symbol;

import java.util.*;

public class ILanguage {

    public final List<IState> states;
    public final List<ITransition> transitions;

    public ILanguage() {
        this.states = new ArrayList<>();
        this.transitions = new ArrayList<>();
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
