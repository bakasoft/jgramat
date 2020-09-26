package gramat.symbols;

import gramat.util.PP;

import java.io.PrintStream;

public class SymbolRange implements Symbol {

    public final char begin;
    public final char end;

    public SymbolRange(char begin, char end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public void printAmCode(PrintStream out) {
        // TODO escape chars
        out.print(begin);
        out.print('-');
        out.print(end);
    }

    @Override
    public boolean test(char c) {
        return c >= begin && c <= end;
    }

    @Override
    public String toString() {
        return "range " + PP.str(begin) + "," + PP.str(end);
    }
}
