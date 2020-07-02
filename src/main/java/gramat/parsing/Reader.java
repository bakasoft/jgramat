package gramat.parsing;

import gramat.common.TextException;
import gramat.common.TextSource;
import gramat.tools.FileTool;

import java.nio.file.Path;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class Reader extends TextSource {

    public Reader(Path file) {
        super(file);
    }

    public Reader(String content) {
        super(content);
    }

    public Reader(String content, String source) {
        super(content, source);
    }

    public Reader(char[] content, String source) {
        super(content, source);
    }

    public boolean isAlive() {
        return position < length;
    }

    public <T> T transaction(Supplier<T> supplier) {
        var pos0 = position;
        var result = supplier.get();

        if (result == null) {
            position = pos0;
        }

        return result;
    }

    public boolean transaction(BooleanSupplier supplier) {
        var pos0 = position;
        var result = supplier.getAsBoolean();

        if (!result) {
            position = pos0;
        }

        return result;
    }

    public void skipBlanks() {
        while (position < length) {
            var c = content[position];

            if (c == ' ' || c == '\n' || c == '\t' || c == '\r') {
                position++;
            }
            else if (c == '/' && position + 1 < length) {
                if (content[position + 1] == '/') {
                    position++;

                    while (position < length) {
                        if (content[position] == '\n') {
                            break;
                        }

                        position++;
                    }
                }
                else if (content[position + 1] == '*') {
                    while (position < length) {
                        if (content[position] == '*') {
                            position++;

                            if (position < length && content[position] == '/') {
                                position++;
                                break;
                            }
                        }
                        else {
                            position++;
                        }
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

    public boolean pull(char c) {
        if (position < length) {
            if (content[position] == c) {
                position++;
                return true;
            }
        }

        return false;
    }

    public boolean test(char c) {
        return position < length && content[position] == c;
    }

    public String readKeyword() {
        StringBuilder keyword = new StringBuilder();

        while (position < length) {
            var c = content[position];

            if (isKeywrodChar(c, keyword.length() == 0)) {
                keyword.append(c);
                position++;
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

    public Integer readInteger() {
        StringBuilder digits = new StringBuilder();

        while (position < length) {
            var c = content[position];

            if (isDigitChar(c)) {
                digits.append(c);
                position++;
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

    public String readString(char delimiter) {
        if (position >= length || content[position] != delimiter) {
            return null;
        }

        position++;

        StringBuilder value = new StringBuilder();

        while(position < length) {
            var c = readStringChar(delimiter);

            if (c == null) {
                break;
            }

            value.append(c);
        }

        if (position >= length || content[position] != delimiter) {
            throw new TextException("Expected delimiter", getLocation());
        }

        position++;

        return value.toString();
    }

    public Character readStringChar(char delimiter) {
        if (position >= length) {
            return null;
        }

        var c = content[position];

        if (c == delimiter) {
            return null;
        }

        position++;

        if (c == '\\') {
            if (position >= length) {
                throw new TextException("Expected escaped char", getLocation());
            }

            var escaped = content[position];

            position++;

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
                var hex = readText(4);
                var cod = Integer.parseInt(hex, 16);
                return (char) cod;
            }
            else {
                throw new TextException("Invalid escaped char", getLocation());
            }
        }
        else if (Character.isISOControl(c)) {
            throw new TextException("Invalid string char", getLocation());
        }

        return c;
    }

    private String readText(int count) {
        StringBuilder text = new StringBuilder();

        while (position < length) {
            char c = content[position];

            text.append(c);

            position++;

            if (text.length() == count) {
                break;
            }
        }

        if (text.length() != count) {
            throw new TextException("expected to read text", getLocation());
        }

        return text.toString();
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
