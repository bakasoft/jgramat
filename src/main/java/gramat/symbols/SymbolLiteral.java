package gramat.symbols;

import java.util.Objects;

public class SymbolLiteral implements Symbol {
    private final char value;

    public SymbolLiteral(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    @Override
    public boolean matches(char value) {
        return this.value == value;
    }

    @Override
    public boolean stacks(Symbol symbol) {
        if (symbol instanceof SymbolLiteral) {
            return this.value == ((SymbolLiteral)symbol).value;
        }
        else if (symbol instanceof SymbolRange) {
            var range = (SymbolRange)symbol;

            return range.start == range.end && range.start == this.value;
        }
        else if (symbol instanceof SymbolUnion) {
            var union = (SymbolUnion)symbol;
            var symbols = union.getSymbols();

            return symbols.size() == 1 && symbols.get(0).stacks(symbol);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "SymbolChar{" +
                "value=" + value +
                '}';
    }
}
