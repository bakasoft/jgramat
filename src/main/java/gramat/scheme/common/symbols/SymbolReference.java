package gramat.scheme.common.symbols;

import java.util.Objects;

public class SymbolReference implements Symbol {

    public final String reference;

    public SymbolReference(String reference) {
        this.reference = reference;
    }

    @Override
    public boolean test(char c) {
        throw new RuntimeException("cannot test a reference: " + reference);
    }

    @Override
    public boolean intersects(Symbol other) {
        if (other instanceof SymbolReference) {
            var otherRef = (SymbolReference)other;

            return Objects.equals(this.reference, otherRef.reference);
        }
        return false;
    }

    @Override
    public String toString() {
        return "reference: " + reference;
    }
}
