package gramat.symbols;

public class SymbolWild extends Symbol {

    @Override
    public boolean test(char c) {
        return false;
    }

    @Override
    public boolean intersects(Symbol other) {
        // Wild symbols can only intersect other wild symbols
        return other instanceof SymbolWild;
    }

    @Override
    public String toString() {
        return "wild";
    }
}
