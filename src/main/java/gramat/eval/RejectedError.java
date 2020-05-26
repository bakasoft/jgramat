package gramat.eval;

import gramat.automata.dfa.DState;
import gramat.util.parsing.Location;

public class RejectedError extends Exception {

    private final DState state;
    private final Location location;

    public RejectedError(DState state, Location location) {
        super(generate_message(state, location));
        this.state = state;
        this.location = location;
    }

    public DState getState() {
        return state;
    }

    public Location getLocation() {
        return location;
    }

    private static String generate_message(DState state, Location location) {
        StringBuilder message = new StringBuilder();

        message.append("Rejected at ");
        message.append(location.toString());
        message.append("\n");

        if (state.transitions.size() > 0) {
            message.append("Expected: ");

            for (var trn : state.transitions) {
                message.append(trn.getSymbol());
                message.append(" ");
            }

            message.append("\n");
        }

        return message.toString();
    }
}
