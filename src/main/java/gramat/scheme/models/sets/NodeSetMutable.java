package gramat.scheme.models.sets;

import gramat.scheme.models.Node;

import java.util.LinkedHashSet;

public class NodeSetMutable extends LinkedHashSet<Node> {

    public void addAll(NodeSet nodes) {
        addAll(nodes.toList());
    }

    public NodeSet build() {
        if (isEmpty()) {
            return new NodeSetEmpty();
        }
        else if (size() == 1) {
            return new NodeSetSingleton(iterator().next());
        }
        return new NodeSetReadOnly(this);
    }

}
