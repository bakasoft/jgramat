package gramat.source;

import gramat.exceptions.UnexpectedCharException;

public interface AmString extends AmBase {

    default String readString() {
        var value = tryString();

        if (value == null) {
            throw new UnexpectedCharException(getTape());
        }

        return value;
    }

    default String tryQuotedString(char delimiter) {
        var tape = getTape();

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
                    else if (chr == '\\') {
                        buffer.append(chr);
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

            skipVoid();

            return buffer.toString();
        }
        return null;
    }

    default String tryKeyword() {
        var tape = getTape();
        if (isKeywordChar(tape.peek())) {
            var buffer = new StringBuilder();

            do {
                var chr = tape.peek();

                tape.move();

                buffer.append(chr);
            } while (isKeywordChar(tape.peek()));

            skipVoid();

            return buffer.toString();
        }
        return null;
    }

    default String tryString() {
        var str = tryQuotedString('\"');
        if (str != null) {
            return str;
        }
        str = tryQuotedString('\'');
        if (str != null) {
            return str;
        }
        return tryKeyword();
    }

    default boolean isKeywordChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_' || c == '-' || c == '+' || c == '.';
    }

}
