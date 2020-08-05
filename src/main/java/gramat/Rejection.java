package gramat;

import gramat.common.TextLocation;
import gramat.engine.deter.DState;

public class Rejection extends Exception {

    private final DState state;
    private final TextLocation location;

    public Rejection(DState state, TextLocation location) {
        super(generate_message(state, location));
        this.state = state;
        this.location = location;
    }

    public DState getState() {
        return state;
    }

    public TextLocation getLocation() {
        return location;
    }

    private static String generate_message(DState state, TextLocation location) {
        StringBuilder message = new StringBuilder();

        message.append("Rejected at ");
        message.append(location.toString());
        message.append("\n");

        if (state.transitions.length > 0) {
            message.append("Expected: ");

            for (var trn : state.transitions) {
                message.append(trn.symbol);
                message.append(" ");
            }

            message.append("\n");
        }

        return message.toString();
    }
}