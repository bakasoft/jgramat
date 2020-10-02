package gramat.graph;

import gramat.symbols.Symbol;

import java.util.Objects;

public class Token {

    public static Token of(Symbol symbol) {
        return new Token(Objects.requireNonNull(symbol), null);
    }

    public static Token of(String reference) {
        return new Token(null, Objects.requireNonNull(reference));
    }

    private final Symbol symbol;
    private final String reference;

    private Token(Symbol symbol, String reference) {
        this.symbol = symbol;
        this.reference = reference;
    }

    public boolean isSymbol() {
        return symbol != null;
    }

    public boolean isReference() {
        return reference != null;
    }

    public Symbol getSymbol() {
        return Objects.requireNonNull(symbol);
    }

    public String getReference() {
        return Objects.requireNonNull(reference);
    }

}
