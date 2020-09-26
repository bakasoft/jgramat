package gramat.symbols;

import gramat.util.PP;

import java.io.PrintStream;

public class SymbolChar implements Symbol {

    public final char value;

    public SymbolChar(char value) {
        this.value = value;
    }

    @Override
    public void printAmCode(PrintStream out) {
        // TODO improve escaping
        if (value == ' ') {
            out.print("SPACE");
        }
        else if (value == ',') {
            out.print("\\,");
        }
        else if (value == '\r') {
            out.print("\\\\r");
        }
        else if (value == '\n') {
            out.print("\\\\n");
        }
        else if (value == '\t') {
            out.print("\\\\t");
        }
        else if (value == '\\') {
            out.print("\\\\");
        }
        else {
            out.print(value);
        }
    }

    @Override
    public boolean test(char c) {
        return this.value == c;
    }

    @Override
    public String toString() {
        return "char " + PP.str(value);
    }
}
