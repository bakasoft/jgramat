package gramat.models.automata;

import gramat.models.test.ModelTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModelMachine {

    public List<ModelState> states;
    public List<ModelTransition> transitions;
    public List<ModelTest> tests;

    public ModelState initial;

    public List<ModelTransition> findTransitionsFrom(ModelState source) {
        var result = new ArrayList<ModelTransition>();

        for (var transition : transitions) {
            if (transition.source == source) {
                result.add(transition);
            }
        }

        return result;
    }

    public ModelState searchState(String id) {
        var result = new ArrayList<ModelState>();

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

    public ModelState mergeState(String id) {
        var state = searchState(id);

        if (state == null) {
            state = new ModelState();
            state.id = id;
            states.add(state);
        }

        return state;
    }
}
