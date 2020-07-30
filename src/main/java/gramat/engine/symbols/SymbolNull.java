package gramat.engine.symbols;

public class SymbolNull extends Symbol {

    @Override
    public boolean matches(char chr) {
        return false;
    }

    @Override
    public String toString() {
        return "¶";
    }
}
