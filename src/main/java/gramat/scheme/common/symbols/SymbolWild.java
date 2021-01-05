package gramat.scheme.common.symbols;

public class SymbolWild implements Symbol {

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
