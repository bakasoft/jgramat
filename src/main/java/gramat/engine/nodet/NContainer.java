package gramat.engine.nodet;

import gramat.engine.Badge;

abstract public class NContainer {

    public final NRoot root;
    public final NStateList states;
    public final NTransitionList transitions;

    private final NContainer parent;

    public NContainer(NRoot root, NContainer parent) {
        this(root, new NStateList(), new NTransitionList(), parent);
    }

    public NContainer(NRoot root, NStateList states, NTransitionList transitions) {
        this(root, states, transitions, null);
    }

    public NContainer(NRoot root, NStateList states, NTransitionList transitions, NContainer parent) {
        this.root = root;
        this.parent = parent;
        this.states = states;
        this.transitions = transitions;
    }

    private void register_state(NState state) {
        states.add(state);

        if (parent != null) {
            parent.register_state(state);
        }
    }

    private void register_transition(NTransition transition) {
        transitions.add(transition);

        if (parent != null) {
            parent.register_transition(transition);
        }
    }

    public NState newState() {
        var state = root.newState();

        register_state(state);

        return state;
    }

    public void newEmptyTransition(NState source, NState target) {
        newEmptyTransition(source, target, null);
    }

    public void newEmptyTransition(NState source, NState target, Badge badge) {
        var symbol = root.symbols.getEmpty();

        register_transition(
                root.newTransition(source, target, symbol, badge)
        );
    }

    public void newCharTransition(NState source, NState target, char value) {
        newCharTransition(source, target, value, null);
    }

    public void newCharTransition(NState source, NState target, char value, Badge badge) {
        var symbol = root.symbols.getChar(value);

        register_transition(
                root.newTransition(source, target, symbol, badge)
        );
    }

    public void newRangeTransition(NState source, NState target, char begin, char end) {
        newRangeTransition(source, target, begin, end, null);
    }

    public void newRangeTransition(NState source, NState target, char begin, char end, Badge badge) {
        var symbol = root.symbols.getRange(begin, end);

        register_transition(
                root.newTransition(source, target, symbol, badge)
        );
    }

    public void newWildTransition(NState source, NState target) {
        newWildTransition(source, target, null);
    }

    public void newWildTransition(NState source, NState target, Badge badge) {
        var symbol = root.symbols.getWild();

        register_transition(
                root.newTransition(source, target, symbol, badge)
        );
    }

}
