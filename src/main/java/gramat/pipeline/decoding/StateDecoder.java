package gramat.pipeline.decoding;

import gramat.scheme.State;
import gramat.scheme.data.automata.StateData;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class StateDecoder {

    private final HashMap<String, State> items;

    public StateDecoder() {
        items = new LinkedHashMap<>();
    }

    public State map(StateData data) {
        var state = items.computeIfAbsent(data.id, State::new);

        // Merge using OR
        state.accepted |= data.accepted;

        return state;
    }

    public State find(StateData data) {
        var state = items.get(data.id);

        if (state == null) {
            throw new RuntimeException();
        }

        return state;
    }

}
