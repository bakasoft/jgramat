package gramat.symbols;

public interface Symbol {

    boolean matches(char value);

    boolean stacks(Symbol symbol);

}
