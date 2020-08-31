package gramat;

import gramat.nodes.NodeFactory;
import gramat.symbols.SymbolFactory;

public class Gramat {

    public final SymbolFactory symbolFactory;
    public final NodeFactory nodeFactory;

    public Gramat() {
        symbolFactory = new SymbolFactory();
        nodeFactory = new NodeFactory();
    }

}
