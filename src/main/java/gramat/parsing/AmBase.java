package gramat.parsing;

import gramat.framework.Component;
import gramat.input.Tape;
import gramat.exceptions.UnexpectedCharException;
import gramat.models.source.ModelCall;

public interface AmBase extends Component {

    ModelCall tryCall(Tape tape);

    default boolean tryToken(Tape tape, char chr) {
        if (tape.peek() == chr) {
            tape.move();
            skipVoid(tape);
            return true;
        }

        return false;
    }

    default void expectToken(Tape tape, char chr) {
        if (!tryToken(tape, chr)) {
            throw new UnexpectedCharException(tape);
        }
    }

    default void skipVoid(Tape tape) {
        while (tape.alive()) {
            var chr = tape.peek();

            if (chr == '#') {
                while (tape.alive() && tape.peek() != '\n') {
                    tape.move();
                }
            }
            else if (chr == ' ' || chr == '\r' || chr == '\n' || chr == '\t') {
                tape.move();
            }
            else {
                break;
            }
        }
    }

}
