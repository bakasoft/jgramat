package gramat.exceptions;

import gramat.input.Tape;
import gramat.util.PP;

public class UnexpectedCharException extends ParseException {

    public UnexpectedCharException(Tape tape) {
        super(generate_message(tape), tape.getLocation());
    }

    private static String generate_message(Tape tape) {
        return String.format("Unexpected character: %s", PP.str(tape.peek()));
    }

}
