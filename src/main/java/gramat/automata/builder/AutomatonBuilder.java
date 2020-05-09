package gramat.automata.builder;

import gramat.automata.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AutomatonBuilder {

    private final List<StateData> states;
    private final List<TransitionData> transitions;
    private final List<StateReference> stateRefs;

    public AutomatonBuilder() {
        states = new ArrayList<>();
        transitions = new ArrayList<>();
        stateRefs = new ArrayList<>();
    }

    public StateReference createState() {
        var data = make_state();
        return make_reference(data);
    }

    public void transitionIf(StateReference source, char value, StateReference target) {
        make_transition(source.data, new ConditionChar(value), target.data);
    }

    public void transitionIf(StateReference source, char begin, char end, StateReference target) {
        make_transition(source.data, new ConditionRange(begin, end), target.data);
    }

    public void transitionElse(StateReference source, StateReference target) {
        make_transition(source.data, new ConditionElse(), target.data);
    }

    private StateReference make_reference(StateData data) {
        var ref = new StateReference(data);
        stateRefs.add(ref);
        return ref;
    }

    private StateData make_state() {
        var state = new StateData();
        states.add(state);
        return state;
    }

    private void make_transition(StateData source, Condition condition, StateData target) {
        transitions.add(new TransitionData(source, condition, target));
    }

    public void replace(StateReference drop, StateReference keep) {
        replace(drop.data, keep.data);
    }

    private StateData replace(StateData drop, StateData keep) {
        if (drop == keep) {
            return keep;
        }

        // Update transitions
        for (var tr : transitions) {
            if (tr.source == drop) {
                tr.source = keep;
            }

            if (tr.target == drop) {
                tr.target = keep;
            }
        }

        // Update references
        for (var ref : stateRefs) {
            if (ref.data == drop) {
                ref.data = keep;
            }
        }

        states.remove(drop);

        return keep;
    }

    public void validate() {
        // Validate transitions and states
        for (var t : transitions) {
            if (!states.contains(t.source)) {
                throw new RuntimeException();
            }
            if (!states.contains(t.target)) {
                throw new RuntimeException();
            }
        }

        // Validate referenes and states
        for (var r : stateRefs) {
            if (!states.contains(r.data)) {
                throw new RuntimeException();
            }
        }
    }

    public void collapse() {
        int reductions;
        do {
            reductions = collapse_transitions();
        }
        while(reductions > 0);
    }

    private int collapse_transitions() {
        var reductions = 0;
        var trans = transitions.toArray(TransitionData[]::new);

        for (int i = 0; i < trans.length; i++) {
            var iTran = trans[i];

            if (iTran != null) {
                trans[i] = null;

                var matching = new ArrayList<TransitionData>();

                for (int j = 0; j < trans.length; j++) {
                    var jTran = trans[j];

                    if (jTran != null && iTran.source == jTran.source && iTran.condition.matches(jTran.condition)) {
                        trans[j] = null;

                        matching.add(jTran);
                    }
                }

                if (!matching.isEmpty()) {
                    join_transitions(iTran, matching);
                    reductions++;
                }
            }
        }

        return reductions;
    }

    private void join_transitions(TransitionData lead, List<TransitionData> trans) {
        StateData source = lead.source;
        StateData target = lead.target;

        // Join source & target states from other transitions
        for (var tran : trans) {
            source = replace(tran.source, source);
            target = replace(tran.target, target);

            transitions.remove(tran);
        }
    }

    public State compile(Segment segment) {
        var statesMap = new HashMap<StateData, State>();

        for (var sd : states) {
            var st = new State(sd == segment.accepted.data);

            statesMap.put(sd, st);
        }

        for (var tb : transitions) {
//            if (tb.target == segment.rejected.data) {
//                continue;
//            }

            var source = statesMap.get(tb.source);
            var target = statesMap.get(tb.target);

            if (source == null || target == null) {
                throw new RuntimeException();
            }

            if (tb.condition instanceof ConditionChar) {
                var cc = (ConditionChar) tb.condition;
                source.addTransition(target, cc.value);
            }
            else if (tb.condition instanceof ConditionRange) {
                var cr = (ConditionRange) tb.condition;
                source.addTransition(target, cr.begin, cr.end);
            }
            else if (tb.condition instanceof ConditionElse) {
                throw new RuntimeException();
            }
            else {
                throw new RuntimeException();
            }
        }

        var result = statesMap.get(segment.initial.data);

        if (result == null) {
            throw new RuntimeException("There is no initial state");
        }

        return result;
    }

}
