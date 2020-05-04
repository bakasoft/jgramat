package gramat.util;

import java.io.IOException;

public class CodeWriter {

    private final Appendable output;
    private final String tabSymbol;
    private final String separatorSymbol;
    private final String breakSymbol;
    private int tabCount;
    private char lastChar;

    public CodeWriter(Appendable output, String tabSymbol) {
        this.output = output;
        this.tabSymbol = tabSymbol;
        this.separatorSymbol = " ";
        this.breakSymbol = "\n";
    }

    protected final void indent(int delta) {
        tabCount += delta;
    }

    protected final void writeChar(char c) {
        try {
            if (tabSymbol != null && lastChar == '\n' && tabCount > 0) {
                output.append(tabSymbol.repeat(tabCount));
            }
            output.append(c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        lastChar = c;
    }

    protected final void writeString(String value) {
        if (value != null) {
            var chars = value.toCharArray();

            for (var c : chars) {
                writeChar(c);
            }
        }
    }

    protected final void prettySeparator() {
        if (tabSymbol != null) {
            writeString(separatorSymbol);
        }
    }

    protected final void prettyBreak() {
        if (tabSymbol != null) {
            writeString(breakSymbol);
        }
    }

    protected final void writeDelimitedString(String value, char delimiter) {
        writeChar(delimiter);

        for (var c : value.toCharArray()) {
            if (c == '\n') {
                writeString("\\n");
            }
            else if (c == '\t') {
                writeString("\\t");
            }
            else if (c == '\f') {
                writeString("\\f");
            }
            else if (c == '\b') {
                writeString("\\b");
            }
            else if (c == delimiter) {
                writeChar('\\');
                writeChar(delimiter);
            }
            else if (Character.isISOControl(c)) {
                var hex = Integer.toHexString(c);
                writeString("\\u");
                if (hex.length() < 4) {
                    writeString("0".repeat(4 - hex.length()));
                }
                writeString(hex);
            }
            else {
                writeChar(c);
            }
        }

        writeChar(delimiter);
    }

}
