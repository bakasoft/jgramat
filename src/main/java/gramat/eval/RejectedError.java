package gramat.eval;

import gramat.epsilon.State;
import gramat.util.parsing.Location;

public class RejectedError extends Exception {

    private final State state;
    private final Location location;

    public RejectedError(State state, Location location) {
        super(generate_message(state, location));
        this.state = state;
        this.location = location;
    }

    public State getState() {
        return state;
    }

    public Location getLocation() {
        return location;
    }

    private static String generate_message(State state, Location location) {
        StringBuilder message = new StringBuilder();

        message.append("Rejected at ");
        message.append(location.toString());
        message.append("\n");

        if (state.transitions.size() > 0) {
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
