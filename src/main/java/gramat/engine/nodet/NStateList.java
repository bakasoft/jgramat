package gramat.engine.nodet;

import gramat.GramatException;

import java.util.*;
import java.util.stream.Collectors;

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

    private void addAll(NStateList items) {
        for (var item : items) {
            add(item);
        }
    }

    public NStateList copy() {
        return new NStateList(this);
    }

    public boolean contains(NState state) {
        return states.contains(state);
    }

    public boolean containsAll(NStateList states) {
        return this.states.containsAll(states.toList());
    }

    public boolean containsAny(NStateList list) {
        for (var state : list) {
            if (states.contains(state)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<NState> iterator() {
        return states.iterator();
    }

    public boolean remove(NState state) {
        return states.remove(state);
    }

    public void replace(NState oldState, NState newState) {
        for (int i = 0; i < states.size(); i++) {
            if (states.get(i) == oldState) {
                states.set(i, newState);
            }
        }
    }

    public void replaceAll(NStateList oldStates, NState newState) {
        for (int i = 0; i < states.size(); i++) {
            if (oldStates.contains(states.get(i))) {
                states.set(i, newState);
            }
        }
    }

    public boolean isEmpty() {
        return states.isEmpty();
    }

    public boolean removeAll(NStateList states) {
        return this.states.removeAll(states.states);
    }

    public List<NState> toList() {
        return Collections.unmodifiableList(states);
    }

    public String computeID() {
        return states.stream().map(s -> s.id).sorted().collect(Collectors.joining("_"));
    }

    public NStateList getEmptyClosure() {
        var closure = new NStateList();
        for (var state : states) {
            closure.addAll(state.getEmptyClosure());
        }
        return closure;
    }

    public NState findByID(String id) {
        for (var state : states) {
            if (Objects.equals(state.id, id)) {
                return state;
            }
        }
        throw new GramatException("State not found" + id);
    }

    public boolean containsID(String id) {
        for (var state : states) {
            if (Objects.equals(state.id, id)) {
                return true;
            }
        }
        return false;
    }

    public String getUniqueID(String baseID) {
        var n = 1;
        var id = baseID;

        while (containsID(id)) {
            id = baseID + "_".repeat(n);

            n++;
        }

        return id;
    }

    public NStateList subtract(Collection<NState> items) {
        var result = new NStateList();

        for (var state : states) {
            if (!items.contains(state)) {
                result.add(state);
            }
        }

        return result;
    }

    public int size() {
        return states.size();
    }
}
