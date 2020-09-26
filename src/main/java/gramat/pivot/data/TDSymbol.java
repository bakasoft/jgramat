package gramat.pivot.data;

import gramat.symbols.Symbol;

import java.io.PrintStream;

public class TDSymbol extends TDActionWrapper {

    public Symbol symbol;

    public TDSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    @Override
    public void printAmCode(PrintStream out) {
        symbol.printAmCode(out);
    }

}
