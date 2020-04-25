package gramat.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class GramatWriter {

    private final Appendable output;
    private final String tabSymbol;

    private int tab;
    private char lastChar;

    public GramatWriter(Appendable output, String tabSymbol) {
        this.output = output;
        this.tabSymbol = tabSymbol;
    }

    public void write(char c) {
        try {
            if (tabSymbol != null && lastChar == '\n' && tab > 0) {
                output.append(tabSymbol.repeat(tab));
            }
            output.append(c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        lastChar = c;
    }

    public void write(String value) {
        if (value != null) {
            var chars = value.toCharArray();

            for (var c : chars) {
                write(c);
            }
        }
    }

    private void separate() {
        if (tabSymbol != null) {
            write(' ');
        }
    }

    private void breakLine() {
        if (tabSymbol != null) {
            write('\n');
        }
    }

    public void writeValue(Object value) {
        if (value == null) {
            write("null");
        }
        else if (value instanceof String) {
            writeString((String)value, '\"');
        }
        else if (value instanceof Map) {
            writeMap((Map<?,?>)value);
        }
        else if (value instanceof Collection) {
            writeCollection((Collection<?>)value);
        }
        else {
            throw new RuntimeException("Not supported value: " + value);
        }
    }

    public void writeCollection(Collection<?> values) {
        write('[');

        tab++;

        var i = 0;

        for (var value : values) {
            if (i > 0) {
                write(',');
            }

            breakLine();
            writeValue(value);
            i++;
        }

        breakLine();

        tab--;

        write(']');
    }

    public void writeMap(Map<?, ?> map) {
        write('{');

        tab++;

        var i = 0;

        for (var entry : map.entrySet()) {
            if (i > 0) {
                write(',');
            }

            breakLine();

            writeValue(entry.getKey());
            write(':');
            separate();
            writeValue(entry.getValue());
            i++;
        }

        breakLine();

        tab--;

        write('}');
    }

    public void writeString(String value, char delimiter) {
        write(delimiter);

        for (var c : value.toCharArray()) {
            if (c == '\n') {
                write("\\n");
            }
            else if (c == '\t') {
                write("\\t");
            }
            else if (c == '\f') {
                write("\\f");
            }
            else if (c == '\b') {
                write("\\b");
            }
            else if (c == delimiter) {
                write('\\');
                write(delimiter);
            }
            else if (Character.isISOControl(c)) {
                var hex = Integer.toHexString(c);
                write("\\u");
                if (hex.length() < 4) {
                    write("0".repeat(4 - hex.length()));
                }
                write(hex);
            }
            else {
                write(c);
            }
        }

        write(delimiter);
    }

    public static String toDelimitedString(String value, char delimiter) {
        var output = new StringBuilder();
        var writer = new GramatWriter(output, null);

        writer.writeString(value, delimiter);

        return output.toString();
    }

}
