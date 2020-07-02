package gramat.engine;

public class SymbolEmpty extends Symbol {
    @Override
    public boolean matches(char chr) {
        return false;
    }

    @Override
    public String toString() {
        return "¶";  // TODO change to ε
    }
}
