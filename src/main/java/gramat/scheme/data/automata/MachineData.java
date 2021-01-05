package gramat.scheme.data.automata;

import gramat.scheme.data.test.TestData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MachineData {

    public List<StateData> states;
    public List<TransitionData> transitions;
    public List<TestData> tests;

    public StateData initial;

    public List<TransitionData> findTransitionsFrom(StateData source) {
        var result = new ArrayList<TransitionData>();

        for (var transition : transitions) {
            if (transition.source == source) {
                result.add(transition);
            }
        }

        return result;
    }

    public StateData searchState(String id) {
        var result = new ArrayList<StateData>();

        for (var state : states) {
            if (Objects.equals(state.id, id)) {
                result.add(state);
            }
        }

        if (result.isEmpty()) {
            return null;
        }
        else if (result.size() != 1) {
            throw new RuntimeException("Ambiguous state: " + id);
        }

        return result.get(0);
    }

    public StateData mergeState(String id) {
        var state = searchState(id);

        if (state == null) {
            state = new StateData();
            state.id = id;
            states.add(state);
        }

        return state;
    }
}
