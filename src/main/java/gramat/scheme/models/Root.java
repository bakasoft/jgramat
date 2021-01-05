package gramat.scheme.models;

import gramat.scheme.models.sets.NodeSet;

public class Root {

    public final Node source;
    public final NodeSet targets;

    public Root(Node source, NodeSet targets) {
        this.source = source;
        this.targets = targets;
    }
}
