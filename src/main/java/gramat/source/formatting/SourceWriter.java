package gramat.source.formatting;

public class SourceWriter {

    private int line;
    private int column;

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public void write(String chars) {
        for (var c : chars.toCharArray()) {
            write(c);
        }
    }

    public void write(char c) {
        System.out.print(c);

        if (c == '\n') {
            line++;
            column = 0;
        }
        else {
            column++;
        }
    }

    public void writeStringChar(char value) {
        if (value == '\"' || value == '\'' || value == '\\') {
            write('\\');
            write(value);
        }
        else if (isPrintable(value)) {
            write(value);
        }
        else {
            writeStringCharEscaped(value);
        }
    }

    public void writeStringCharEscaped(char value) {
        var hex = Integer.toHexString(value);

        write('\\');
        write('u');

        for (int i = 0; i < 4 - hex.length(); i++) {
            write('0');
        }

        write(hex);
    }

    private static boolean isPrintable(char value) {
        return value >= 0x21 && value <= 0x7E;
    }

    public int getGuideColumn() {
        return 80;
    }
}
