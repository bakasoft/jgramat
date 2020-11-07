package gramat.parsing;

import gramat.input.Tape;
import gramat.exceptions.UnexpectedCharException;

public interface AmString extends AmBase {

    default String readString(Parser parser) {
        var value = tryString(parser);

        if (value == null) {
            throw new UnexpectedCharException(parser.tape);
        }

        return value;
    }

    default String tryQuotedString(Parser parser, char delimiter) {
        if (parser.tape.peek() == delimiter) {
            var buffer = new StringBuilder();

            parser.tape.move();

            while (parser.tape.peek() != delimiter) {
                var chr = parser.tape.read();

                if (chr == '\\') {
                    chr = parser.tape.read();

                    if (chr == delimiter) {
                        buffer.append(delimiter);
                    }
                    else if (chr == 'u') {
                        var code = (char)Integer.parseInt(new String(new char[] {
                                parser.tape.read(), parser.tape.read(), parser.tape.read(), parser.tape.read(),
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
                        throw new UnexpectedCharException(parser.tape, -1);
                    }
                }
                else {
                    buffer.append(chr);
                }
            }

            if (parser.tape.peek() != delimiter) {
                throw new RuntimeException();
            }

            parser.tape.move();

            skipVoid(parser);

            return buffer.toString();
        }
        return null;
    }

    default String tryKeyword(Parser parser) {
        if (isKeywordChar(parser.tape.peek())) {
            var buffer = new StringBuilder();

            do {
                var chr = parser.tape.peek();

                parser.tape.move();

                buffer.append(chr);
            } while (isKeywordChar(parser.tape.peek()));

            skipVoid(parser);

            return buffer.toString();
        }
        return null;
    }

    default String tryString(Parser parser) {
        var str = tryQuotedString(parser, '\"');
        if (str != null) {
            return str;
        }
        str = tryQuotedString(parser, '\'');
        if (str != null) {
            return str;
        }
        return tryKeyword(parser);
    }

    default boolean isKeywordChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_' || c == '-' || c == '+' || c == '.';
    }

}
