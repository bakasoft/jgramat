package gramat.epsilon;

import gramat.automata.raw.RawAutomaton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Builder {

    public final Maker assembler;

    private final Language language;
    private final Builder parent;

    private final Set<State> states;
    private final List<Transition> transitions;

    public Builder(Maker assembler, Language language, Builder parent) {
        this.assembler = assembler;
        this.language = language;
        this.parent = parent;
        this.states = new HashSet<>();
        this.transitions = new ArrayList<>();
    }

    private void register(State state) {
        states.add(state);

        if (parent != null) {
            parent.register(state);
        }
    }

    private void register(Transition transition) {
        transitions.add(transition);

        if (parent != null) {
            parent.register(transition);
        }
    }

    public State newState() {
        var state = language.newState();

        register(state);

        return state;
    }

    public void newNullTransition(State source, State target) {
        var symbol = language.symbols.getEmpty();
        var transition = language.newTransition(source, target, symbol);

        register(transition);
    }

    public void newCharTransition(State source, State target, char ch) {
        var symbol = language.symbols.getChar(ch);
        var transition = language.newTransition(source, target, symbol);

        register(transition);
    }

    public void newRangeTransition(State source, State target, char begin, char end) {
        var symbol = language.symbols.getRange(begin, end);
        var transition = language.newTransition(source, target, symbol);

        register(transition);
    }

    public void newWildTransition(State source, State target) {
        var symbol = language.symbols.getWild();
        var transition = language.newTransition(source, target, symbol);

        register(transition);
    }

    public Machine machine(RawAutomaton content, State initial) {
        var accepted = content.build(this, initial);

        // freeze states & transitions
        var machineStates = new HashSet<>(states);
        var machineTransitions = new ArrayList<>(transitions);

        // be sure initial state is in the states set
        machineStates.add(initial);

        return new Machine(initial, accepted, machineStates, machineTransitions);
    }
}
