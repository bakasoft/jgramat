package gramat.parsing;

import gramat.input.Tape;
import gramat.input.errors.UnexpectedCharException;

public interface AmString extends AmBase {

    default String readString(Tape tape) {
        var value = tryString(tape);

        if (value == null) {
            throw new RuntimeException("unexpected char at " + tape.getLocation());
        }

        return value;
    }

    default String tryString(Tape tape) {
        if (tape.peek() == '\"') {
            var buffer = new StringBuilder();

            tape.move();

            while (tape.peek() != '\"') {
                var chr = tape.read();

                if (chr == '\\') {
                    chr = tape.read();

                    if (chr == 'u') {
                        var code = (char)Integer.parseInt(new String(new char[] {
                                tape.read(), tape.read(), tape.read(), tape.read(),
                        }), 16);

                        buffer.append(code);
                    }
                    else {
                        throw new UnexpectedCharException(tape);
                    }
                }
                else {
                    buffer.append(chr);
                }
            }

            if (tape.peek() != '\"') {
                throw new RuntimeException();
            }

            tape.move();

            skipVoid(tape);

            return buffer.toString();
        }
        else if (Character.isLetterOrDigit(tape.peek())) {
            var buffer = new StringBuilder();

            do {
                var chr = tape.peek();

                tape.move();

                buffer.append(chr);
            } while (Character.isLetterOrDigit(tape.peek()));

            skipVoid(tape);

            return buffer.toString();
        }

        return null;
    }

}
