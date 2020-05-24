package gramat.automata.ndfa;

import java.util.*;

public class NStateSet implements Iterable<NState> {

    private final Collection<NState> states;

    public NStateSet() {
        states = new ArrayList<>();
    }

    public static NStateSet of(NState source) {
        var result = new NStateSet();
        result.add(source);
        return result;
    }

    public static NStateSet of(NState[] states) {
        var result = new NStateSet();
        Collections.addAll(result.states, states);
        return result;
    }

    public void add(NState state) {
        if (!states.contains(state)) {
            states.add(state);
        }
    }

    public void add(List<NState> states) {
        for (var state : states) {
            add(state);
        }
    }

    public void add(NStateSet states) {
        for (var state : states.states) {
            add(state);
        }
    }

    @Override
    public Iterator<NState> iterator() {
        return states.iterator();
    }

    public boolean isEmpty() {
        return states.isEmpty();
    }

    public NState[] toArray() {
        return states.toArray(NState[]::new);
    }

    public void notEmpty(NContext context) {
        if (states.isEmpty()) {
            states.add(context.state());
        }
    }
}
