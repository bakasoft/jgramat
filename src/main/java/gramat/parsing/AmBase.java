package gramat.parsing;

import gramat.input.Tape;
import gramat.exceptions.UnexpectedCharException;
import gramat.models.source.ModelCall;

public interface AmBase {

    ModelCall tryCall(Parser parser);

    default boolean tryToken(Parser parser, char chr) {
        if (parser.tape.peek() == chr) {
            parser.tape.move();
            skipVoid(parser);
            return true;
        }

        return false;
    }

    default void expectToken(Parser parser, char chr) {
        if (!tryToken(parser, chr)) {
            throw new UnexpectedCharException(parser.tape);
        }
    }

    default void skipVoid(Parser parser) {
        while (parser.tape.alive()) {
            var chr = parser.tape.peek();

            if (chr == '#') {
                while (parser.tape.alive() && parser.tape.peek() != '\n') {
                    parser.tape.move();
                }
            }
            else if (chr == ' ' || chr == '\r' || chr == '\n' || chr == '\t') {
                parser.tape.move();
            }
            else {
                break;
            }
        }
    }

}
