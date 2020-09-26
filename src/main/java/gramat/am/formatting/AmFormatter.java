package gramat.am.formatting;

import gramat.symbols.Symbol;
import gramat.symbols.SymbolChar;
import gramat.symbols.SymbolRange;
import gramat.symbols.SymbolWild;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class AmFormatter {

    private final Pattern KEYWORD = Pattern.compile("[a-zA-Z0-9_.+-]+");

    private final Appendable output;

    private char lastChar;

    public AmFormatter(Appendable output) {
        this.output = output;
    }

    public final void write(char c) {
        try {
            output.append(c);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        lastChar = c;
    }

    public void write(String text) {
        for (var c : text.toCharArray()) {
            write(c);
        }
    }

    public void initial(String id) {
        write("I ");
        value(id);
        end();
    }

    public void accepted(String id) {
        write("A ");
        value(id);
        end();
    }

    public void transition(String source, String target) {
        write("T ");
        value(source);
        write(',');
        value(target);
    }

    public void action(String name, List<String> args) {
        write("\n\tR ");
        value(name);
        write('(');

        for (var i = 0; i < args.size(); i++) {
            if (i > 0) {
                write(", ");
            }

            value(args.get(i));
        }

        write(')');
    }

    public void symbol(Symbol symbol) {
        write("\n\tS ");

        if (symbol instanceof SymbolWild) {
            write('*');
        }
        else if (symbol instanceof SymbolRange) {
            var range = (SymbolRange)symbol;

            value(String.valueOf(range.begin));
            write(',');
            value(String.valueOf(range.end));
        }
        else if (symbol instanceof SymbolChar) {
            var chr = (SymbolChar)symbol;

            value(String.valueOf(chr.value));
        }
        else {
            throw new RuntimeException();
        }
    }

    private void check_space() {
        if (!Character.isWhitespace(lastChar)) {
            write(' ');
        }
    }

    public void end() {
        write(";\n");
    }

    public void value(String value) {
        if (KEYWORD.matcher(value).matches()) {
            write(value);
        }
        else {
            write('\"');

            for (var c : value.toCharArray()) {
                if (c == '\"' || c == '\'' || c == '\\') {
                    write('\\');
                    write(c);
                }
                else if (c == '\n') {
                    write("\\n");
                }
                else if (c == '\r') {
                    write("\\r");
                }
                else if (c == '\t') {
                    write("\\t");
                }
                else if (c >= 0x20 && c <= 0x7E) {
                    write(c);
                }
                else {
                    write("\\u");
                    var hex = Integer.toHexString(c);
                    if (hex.length() < 4) {
                        write("0".repeat(4 - hex.length()));
                    }
                    write(hex);
                }
            }

            write('\"');
        }
    }

    public void space() {
        write(' ');
    }

    public void newLine() {
        write('\n');
    }

}
