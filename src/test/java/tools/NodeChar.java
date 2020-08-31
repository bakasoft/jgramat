package tools;

import gramat.nodes.Node;
import gramat.nodes.NodeAdapter;

public class NodeChar extends NodeAdapter {

    public final char value;

    public NodeChar(char value) {
        this.value = value;
    }

    @Override
    public Node tryStack(Node other) {
        if (other instanceof NodeChar) {
            var n = (NodeChar)other;

            if (n.value == this.value) {
                return this;
            }
        }
        return null;
    }

}