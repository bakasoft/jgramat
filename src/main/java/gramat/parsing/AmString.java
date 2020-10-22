package gramat.parsing;

import gramat.input.Tape;
import gramat.exceptions.UnexpectedCharException;

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
                    else if (chr == 's') {
                        buffer.append(' ');
                    }
                    else if (chr == 't') {
                        buffer.append('\t');
                    }
                    else if (chr == 'n') {
                        buffer.append('\n');
                    }
                    else if (chr == 'r') {
                        buffer.append('\r');
                    }
                    else {
                        throw new UnexpectedCharException(tape, -1);
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
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_' || c == '-' || c == '+' || c == '.';
    }

}
