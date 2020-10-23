package gramat.formatting;

import java.util.regex.Pattern;

public class AmFormatter extends BaseFormatter {

    private final Pattern KEYWORD = Pattern.compile("[a-zA-Z0-9_.+-]+");

    public AmFormatter(Appendable output) {
        super(output);
    }

    protected final void raw(String... items) {
        for (var item : items) {
            raw(item);
        }
    }

    protected final void raw(String text) {
        for (var c : text.toCharArray()) {
            write(c);
        }
    }

    protected final void sp() {
        write(' ');
    }

    protected final void ln() {
        write('\n');
    }

    protected final void str(String value) {
        if (KEYWORD.matcher(value).matches()) {
            write(value);
        }
        else {
            writeJsonString(value);
        }
    }

    protected final void amstr(String str) {
        raw(str.replace("\\", "\\\\")
                .replace(",", "\\,")
                .replace(":", "\\:")
                .replace("\n", "\\\n"));
    }

}
