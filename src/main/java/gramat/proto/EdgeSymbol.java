package gramat.proto;

import gramat.symbols.Symbol;

public class EdgeSymbol extends Edge {

    public final Symbol symbol;

    public EdgeSymbol(Vertex source, Vertex target, Symbol symbol) {
        super(source, target);
        this.symbol = symbol;
    }

}
