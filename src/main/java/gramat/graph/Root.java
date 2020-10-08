package gramat.graph;

import gramat.util.Chain;

public class Root {

    public final Node source;
    public final Chain<Node> targets;

    public Root(Node source, Chain<Node> targets) {
        this.source = source;
        this.targets = targets;
    }
}
