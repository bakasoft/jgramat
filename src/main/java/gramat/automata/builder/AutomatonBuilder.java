package gramat.automata.builder;

import gramat.automata.State;

import java.util.ArrayList;

public class AutomatonBuilder {

    private final ArrayList<StateBuilder> states;
    private final ArrayList<TransitionBuilder> transitions;

    protected boolean acceptedValue;
    protected boolean rejectedValue;

    public AutomatonBuilder() {
        states = new ArrayList<>();
        transitions = new ArrayList<>();
        acceptedValue = true;
        rejectedValue = false;
    }

    public SegmentBuilder createSegment(StateBuilder s0, StateBuilder sF) {
        return new SegmentBuilder(this, s0, sF);
    }

    public StateBuilder createState() {
        var state = new StateBuilder(this);
        states.add(state);
        return state;
    }

    public TransitionBuilder createTransition(StateBuilder source, char value, StateBuilder target) {
        return _create_transition(source, new ConditionChar(value), target);
    }

    public TransitionBuilder createTransition(StateBuilder source, char begin, char end, StateBuilder target) {
        return _create_transition(source, new ConditionRange(begin, end), target);
    }

    private TransitionBuilder _create_transition(StateBuilder source, Condition condition, StateBuilder target) {
        var t = new TransitionBuilder(source, condition, target);
        transitions.add(t);
        return t;
    }

    public void replace(StateBuilder oldState, StateBuilder newState) {
        if (newState.accepted == null) {
            newState.accepted = oldState.accepted;
        }
        else if (oldState.accepted != null && newState.accepted != oldState.accepted) {
            throw new RuntimeException();
        }

        var trs = transitions.toArray(TransitionBuilder[]::new);

        for (var tr : trs) {
            if (tr.source == oldState && tr.target == oldState) {
                transitions.remove(tr);
                transitions.add(new TransitionBuilder(newState, tr.condition, newState));
            }
            else if (tr.source == oldState) {
                transitions.remove(tr);
                transitions.add(new TransitionBuilder(newState, tr.condition, tr.target));
            }
            else if (tr.target == oldState) {
                transitions.remove(tr);
                transitions.add(new TransitionBuilder(tr.source, tr.condition, newState));
            }
        }
    }

    public TransitionBuilder[] getTransitions() {
        return transitions.toArray(TransitionBuilder[]::new);
    }

    public StateBuilder[] getStates() {
        return states.toArray(StateBuilder[]::new);
    }
}
