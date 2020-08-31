package gramat.symbols;

public class SymbolNot implements Symbol {

    private final Symbol notSymbol;

    public SymbolNot(Symbol notSymbol) {
        this.notSymbol = notSymbol;
    }

    public Symbol getSymbol() {
        return notSymbol;
    }

    @Override
    public boolean matches(char value) {
        return !notSymbol.matches(value);
    }

    @Override
    public boolean stacks(Symbol symbol) {
        if (symbol instanceof SymbolNot) {
            var not = (SymbolNot)symbol;

            return notSymbol.stacks(not.notSymbol);
        }
        return false;
    }
}
