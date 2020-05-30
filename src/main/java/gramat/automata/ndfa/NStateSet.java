package gramat.automata.ndfa;

import java.util.*;

public class NStateSet implements Iterable<NState> {

    private final ArrayList<NState> states;

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
        for (NState state : states) {
            result.add(state);
        }
        return result;
    }

    public static NStateSet of(Collection<NState> states) {
        var result = new NStateSet();
        for (NState state : states) {
            result.add(state);
        }
        return result;
    }

    public boolean add(NState state) {
        if (!this.states.contains(state)) {
            return this.states.add(state);
        }
        return false;
    }

    public void add(List<NState> states) {
        for (var state : states) {
            add(state);
        }
    }

    public void add(NState... items) {
        for (var state : items) {
            add(state);
        }
    }

    public void add(NStateSet... sets) {
        for (var set : sets) {
            for (var state : set.states) {
                add(state);
            }
        }
    }

    @Override
    public Iterator<NState> iterator() {
        return states.iterator();
    }

    public boolean isEmpty() {
        return states.isEmpty();
    }

    public boolean isNotEmpty() {
        return states.size() > 0;
    }

    public List<NState> toList() {
        return Collections.unmodifiableList(states);
    }

    public void notEmpty(NContext context) {
        if (states.isEmpty()) {
            states.add(context.language.state());
        }
    }

    public boolean contains(NState source) {
        return states.contains(source);
    }

    public void remove(NStateSet states) {
        for (var state : states) {
            this.states.remove(state);
        }
    }

    public String getHash() {
        var ids = new int[states.size()];
        var i = 0;

        for (var state : states) {
            ids[i] = state.id;
            i++;
        }

        Arrays.sort(ids);

        var output = new StringBuilder();

        for (i = 0; i < ids.length; i++) {
            if (i > 0) {
                output.append('|');
            }

            output.append(ids[i]);
        }

        return output.toString();
    }

    public NStateSet getNullClosure() {
        var closure = new NStateSet();

        for (var state : states) {
            closure.add(state.getNullClosure());
        }

        return closure;
    }
}
