package gramat.engine.nodet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class NStateList implements Iterable<NState> {

    private final ArrayList<NState> states;

    public NStateList() {
        states = new ArrayList<>();
    }

    public NStateList(NState... states) {
        this.states = new ArrayList<>();
        Collections.addAll(this.states, states);
    }

    private NStateList(NStateList states) {
        this.states = new ArrayList<>(states.states);
    }

    public boolean add(NState state) {
        if (states.contains(state)) {
            return false;
        }
        return states.add(state);
    }

    public NStateList copy() {
        return new NStateList(this);
    }

    public boolean contains(NState state) {
        return states.contains(state);
    }

    @Override
    public Iterator<NState> iterator() {
        return states.iterator();
    }
}
