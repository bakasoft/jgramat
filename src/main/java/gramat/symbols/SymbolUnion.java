package gramat.symbols;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SymbolUnion implements Symbol {

    private final List<Symbol> symbols;

    public SymbolUnion(List<Symbol> symbols) {
        this.symbols = new ArrayList<>(symbols);
    }

    public List<Symbol> getSymbols() {
        return Collections.unmodifiableList(symbols);
    }

    @Override
    public boolean matches(char value) {
        for (var symbol : symbols) {
            if (symbol.matches(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean stacks(Symbol symbol) {
        if (symbols.size() == 1) {
            return symbols.get(0).stacks(symbol);
        }
        if (symbol instanceof SymbolUnion) {
            var other = (SymbolUnion)symbol;

            // if all of this symbols can stack with the other symbols
            for (var thisSymbol : this.symbols) {
                var canStack = false;

                for (var otherSymbol : other.getSymbols()) {
                    if (thisSymbol.stacks(otherSymbol)) {
                        canStack = true;
                        break;
                    }
                }

                if (!canStack) {
                    return false;
                }
            }

            // and all the other symbols can stack with this symbols
            for (var otherSymbol : other.getSymbols()) {
                var canStack = false;

                for (var thisSymbol : this.symbols) {
                    if (otherSymbol.stacks(thisSymbol)) {
                        canStack = true;
                        break;
                    }
                }

                if (!canStack) {
                    return false;
                }
            }

            // both unions can stack
            return true;
        }
        return false;
    }
}
