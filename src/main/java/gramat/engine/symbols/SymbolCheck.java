package gramat.engine.symbols;

import gramat.engine.checks.Check;

import java.util.Objects;

public class SymbolCheck extends Symbol {

    public final Symbol symbol;
    public final Check check;

    SymbolCheck(Symbol symbol, Check check) {
        this.symbol = Objects.requireNonNull(symbol);
        this.check = Objects.requireNonNull(check);
    }

    @Override
    public String toString() {
        return symbol + "/" + check;
    }
}
