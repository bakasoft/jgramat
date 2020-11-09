package gramat.pipeline.decoding;

import gramat.scheme.machine.State;
import gramat.scheme.models.automata.ModelState;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class StateDecoder {

    private final HashMap<String, State> items;

    public StateDecoder() {
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
