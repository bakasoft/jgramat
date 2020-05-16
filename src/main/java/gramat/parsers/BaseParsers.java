package gramat.parsers;

import gramat.util.parsing.ParseException;
import gramat.util.parsing.Source;

import java.util.regex.Pattern;

public class BaseParsers {

    public static void skipBlanks(Source source) {
        while (source.alive()) {
            var c = source.peek();

            if (c == Source.EOF) {
                return;
            }
            else if (c == ' ' || c == '\n' || c == '\t' || c == '\r') {
                source.moveNext();
            }
            else if (c == '/') {
                if (source.pull("//")) {
                    while (source.alive()) {
                        if (source.peek() == '\n') {
                            break;
                        }

                        source.moveNext();
                    }
                }
                else if (source.pull("/*")) {
                    while (source.alive()) {
                        if (source.pull("*/")) {
                            break;
                        }

                        source.moveNext();
                    }
                }
                else {
                    return;
                }
            }
            else {
                return;
            }
        }
    }

    public static String readKeyword(Source source) {
        StringBuilder keyword = new StringBuilder();

        while (source.alive()) {
            var c = source.peek();

            if (c != Source.EOF && isKeywrodChar(c, keyword.length() == 0)) {
                keyword.append((char)c);
                source.moveNext();
            }
            else {
                break;
            }
        }

        if (keyword.length() == 0) {
            return null;
        }

        return keyword.toString();
    }

    public static Integer readInteger(Source source) {
        StringBuilder digits = new StringBuilder();

        while (source.alive()) {
            var c = source.peek();

            if (c != Source.EOF && isDigitChar(c)) {
                digits.append((char)c);
                source.moveNext();
            }
            else {
                break;
            }
        }

        if (digits.length() == 0) {
            return null;
        }

        return Integer.parseInt(digits.toString());
    }

    public static String readString(Source source, char delimiter) {
        if (!source.pull(delimiter)) {
            return null;
        }

        StringBuilder value = new StringBuilder();

        while(source.alive()) {
            var c = readStringChar(source, delimiter);

            if (c == Source.EOF) {
                break;
            }

            value.append((char)c);
        }

        source.expect(delimiter);

        return value.toString();
    }

    public static int readStringChar(Source source, char delimiter) {
        var c = source.peek();

        if (c == Source.EOF || c == delimiter) {
            return Source.EOF;
        }

        source.moveNext();

        if (c == '\\') {
            var escaped = source.peek();

            if (escaped == Source.EOF) {
                throw source.error("Expected an escaped char");
            }

            source.moveNext();

            // when it is a direct escaping
            if (escaped == delimiter
                    || escaped == '\\' || escaped == '\"' || escaped == '\''
                    || escaped == '`' || escaped == '/') {
                return escaped;
            }
            else if (escaped == 'n') {
                return '\n';
            }
            else if (escaped == 'r') {
                return '\r';
            }
            else if (escaped == 't') {
                return '\t';
            }
            else if (escaped == 'b') {
                return '\b';
            }
            else if (escaped == 'f') {
                return '\f';
            }
            else if (escaped == 's') {
                return ' ';
            }
            else if (escaped == 'u') {
                var hex = source.readText(4);
                var cod = Integer.parseInt(hex, 16);

                return cod;
            }
            else {
                throw source.error("Invalid escaped char");
            }
        }
        else if (Character.isISOControl(c)) {
            throw source.error("Invalid string char");
        }

        return c;
    }

    private static boolean isKeywrodChar(int c, boolean isFirstChar) {
        return (!isFirstChar && c >= '0' && c <= '9')
                || (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z')
                || c == '_'
                || c == '-';
    }

    private static boolean isDigitChar(int c) {
        return (c >= '0' && c <= '9');
    }

}
