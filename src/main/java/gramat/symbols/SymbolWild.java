package gramat.symbols;

public class SymbolWild extends Symbol {

    @Override
    public boolean test(char c) {
        return false;
    }

    @Override
    public String toString() {
        return "wild";
    }
}
