package gramat.pipeline.parsing;

import gramat.exceptions.UnexpectedCharException;
import gramat.input.Tape;
import gramat.scheme.models.parsing.ModelCall;
import gramat.parsers.ValueParser;

import java.util.List;

public interface BaseParser {

    String loadValue(String valueDirective, List<Object> arguments);

    ValueParser findParser(String name);

    Tape getTape();

    ModelCall tryCall();

    default boolean tryToken(char chr) {
        var tape = getTape();
        if (tape.peek() == chr) {
            tape.move();
            skipVoid();
            return true;
        }

        return false;
    }

    default void expectToken(char chr) {
        var tape = getTape();
        if (!tryToken(chr)) {
            throw new UnexpectedCharException(tape);
        }
    }

    default void skipVoid() {
        var tape = getTape();
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
