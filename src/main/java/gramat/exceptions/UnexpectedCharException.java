package gramat.exceptions;

import gramat.input.Tape;
import gramat.util.PP;

public class UnexpectedCharException extends ParseException {

    public UnexpectedCharException(Tape tape) {
        super(generate_message(tape), tape.getLocation());
    }

    public UnexpectedCharException(Tape tape, int offset) {
        super(generate_message(tape), tape.locationOf(tape.getPosition() + offset));
    }

    private static String generate_message(Tape tape) {
        return String.format("Unexpected character: %s", PP.str(tape.peek()));
    }

}
