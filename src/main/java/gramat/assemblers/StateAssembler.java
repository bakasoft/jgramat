package gramat.assemblers;

import gramat.machine.State;
import gramat.models.automata.ModelState;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class StateAssembler {

    private final HashMap<String, State> items;

    public StateAssembler() {
        items = new LinkedHashMap<>();
    }

    public State map(ModelState model) {
        var state = items.computeIfAbsent(model.id, State::new);

        // Merge using OR
        state.accepted |= model.accepted;

        return state;
    }

    public State find(ModelState model) {
        var state = items.get(model.id);

        if (state == null) {
            throw new RuntimeException();
        }

        return state;
    }

}
