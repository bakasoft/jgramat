package gramat.nodes;

import gramat.Context;

import java.util.List;

public class NodeAdapter implements Node {

    @Override
    public void eval(Context context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean test(char value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Node collapse(NodeContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Node compile(NodeContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Node tryStack(Node other) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<NodeVertex> toVertices() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Node> getNodes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOptional() {
        throw new UnsupportedOperationException();
    }

}