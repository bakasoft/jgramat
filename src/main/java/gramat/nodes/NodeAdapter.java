package gramat.nodes;

import gramat.Context;

import java.util.List;

public class NodeAdapter extends Node {

    @Override
    protected void eval_impl(Context context) {
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
    public Node stack(Node other) {
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

    @Override
    public Node shallowCopy() {
        throw new UnsupportedOperationException();
    }

}