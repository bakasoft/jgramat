package gramat.proto;

import gramat.symbols.Symbol;

public class LinkSymbol extends Link {

    public final Symbol symbol;

    public LinkSymbol(Node source, Node target, Symbol symbol) {
        super(source, target);
        this.symbol = symbol;
    }

}
