package gramat.am.formatting;

import java.io.IOException;
import java.util.regex.Pattern;

public class AmFormatter {

    private final Pattern KEYWORD = Pattern.compile("[a-zA-Z0-9_.+-]+");

    private final Appendable output;

    public AmFormatter(Appendable output) {
        this.output = output;
    }

    protected final void raw(String... items) {
        for (var item : items) {
            raw(item);
        }
    }

    protected final void raw(String text) {
        try {
            for (var c : text.toCharArray()) {
                output.append(c);
            }
        }
        catch (IOException e) {
            throw new RuntimeException();
        }
    }

    protected final void sp() {
        try {
            output.append(' ');
        }
        catch (IOException e) {
            throw new RuntimeException();
        }
    }

    protected final void ln() {
        try {
            output.append('\n');
        }
        catch (IOException e) {
            throw new RuntimeException();
        }
    }

    protected final void str(String value) {
        try {
            if (KEYWORD.matcher(value).matches()) {
                output.append(value);
            }
            else {
                output.append('\"');

                for (var c : value.toCharArray()) {
                    if (c == '\"' || c == '\'' || c == '\\') {
                        output.append('\\');
                        output.append(c);
                    }
                    else if (c == '\n') {
                        output.append("\\n");
                    }
                    else if (c == '\r') {
                        output.append("\\r");
                    }
                    else if (c == '\t') {
                        output.append("\\t");
                    }
                    else if (c >= 0x20 && c <= 0x7E) {
                        output.append(c);
                    }
                    else {
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
        }
        catch (IOException e) {
            throw new RuntimeException();
        }
    }

    protected final void amstr(String str) {
        raw(str.replace("\\", "\\\\")
                .replace(",", "\\,")
                .replace(":", "\\:")
                .replace("\n", "\\\n"));
    }

}
