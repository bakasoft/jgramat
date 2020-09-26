package gramat.symbols;

import java.io.PrintStream;

public class SymbolWild implements Symbol {
    @Override
    public void printAmCode(PrintStream out) {
        out.print('*');
    }

    @Override
    public boolean test(char c) {
        return false;
    }

    @Override
    public String toString() {
        return "wild";
    }
}
