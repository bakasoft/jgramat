package gramat.parsing;

import gramat.input.Tape;
import gramat.input.errors.UnexpectedCharException;

public interface AmString extends AmBase {

    default String readString(Tape tape) {
        var value = tryString(tape);

        if (value == null) {
            throw new UnexpectedCharException(tape);
        }

        return value;
    }

    default String tryQuotedString(Tape tape, char delimiter) {
        if (tape.peek() == delimiter) {
            var buffer = new StringBuilder();

            tape.move();

            while (tape.peek() != delimiter) {
                var chr = tape.read();

                if (chr == '\\') {
                    chr = tape.read();

                    if (chr == delimiter) {
                        buffer.append(delimiter);
                    }
                    else if (chr == 'u') {
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

            if (tape.peek() != delimiter) {
                throw new RuntimeException();
            }

            tape.move();

            skipVoid(tape);

            return buffer.toString();
        }
        return null;
    }

    default String tryKeyword(Tape tape) {
        if (isKeywordChar(tape.peek())) {
            var buffer = new StringBuilder();

            do {
                var chr = tape.peek();

                tape.move();

                buffer.append(chr);
            } while (isKeywordChar(tape.peek()));

            skipVoid(tape);

            return buffer.toString();
        }
        return null;
    }

    default String tryString(Tape tape) {
        var str = tryQuotedString(tape, '\"');
        if (str != null) {
            return str;
        }
        str = tryQuotedString(tape, '\'');
        if (str != null) {
            return str;
        }
        return tryKeyword(tape);
    }

    default boolean isKeywordChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_' || c == '-' || c == '+' || c == '.';
    }

}
