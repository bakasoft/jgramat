package gramat.util;

import java.io.IOException;
import java.util.function.Supplier;

public class Debugger {

    private final Appendable out;
    private int indentation;

    private char lastChar;

    private Supplier<String> suffixSupplier;

    public Debugger() {
        this(System.out);
    }

    public Debugger(Appendable out) {
        this.out = out;
        this.indentation = 0;
    }

    public void suffix(Supplier<String> suffixSupplier) {
        this.suffixSupplier = suffixSupplier;
    }

    private void write(char c) {
        try {
            if (lastChar == '\n' && indentation > 0) {
                out.append("  ".repeat(indentation));
            }

            out.append(c);

            lastChar = c;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void write(String value) {
        if (value != null) {
            for (char c : value.toCharArray()) {
                write(c);
            }
        }
    }

    private String checkSuffix() {
        return suffixSupplier != null ? suffixSupplier.get() : null;
    }

    public void separate() {
        if (lastChar != ' ') {
            write(' ');
        }
    }

    public void breakLine() {
        if (lastChar != '\n') {
            write('\n');
        }
    }

    public void indent(int delta) {
        this.indentation += delta;
    }

    public void log(String message) {
        write(message);

        var suffix = checkSuffix();

        if (suffix != null) {
            separate();
            write(suffix);
        }

        breakLine();
    }

}
