package gramat.formatting;

import java.io.IOException;

public class BaseFormatter {

    private final Appendable output;

    public BaseFormatter(Appendable output) {
        this.output = output;
    }

    protected void write(char c) {
        try {
            output.append(c);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void write(String value) {
        try {
            output.append(value);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void writeJsonString(String value) {
        try {
            output.append('\"');

            for (var c : value.toCharArray()) {
                if (c == '\"' || c == '\'' || c == '\\') {
                    output.append('\\');
                    output.append(c);
                } else if (c == '\n') {
                    output.append("\\n");
                } else if (c == '\r') {
                    output.append("\\r");
                } else if (c == '\t') {
                    output.append("\\t");
                } else if (c >= 0x20 && c <= 0x7E) {
                    output.append(c);
                } else {
                    output.append("\\u");
                    var hex = Integer.toHexString(c);
                    if (hex.length() < 4) {
                        output.append("0".repeat(4 - hex.length()));
                    }
                    output.append(hex);
                }
            }

            output.append('\"');
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
