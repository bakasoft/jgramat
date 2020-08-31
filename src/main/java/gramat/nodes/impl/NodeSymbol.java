package gramat.nodes.impl;

import gramat.Context;
import gramat.InvalidSyntaxException;
import gramat.nodes.Node;
import gramat.nodes.NodeContext;
import gramat.nodes.NodeVertex;
import gramat.symbols.Symbol;

import java.util.List;

public class NodeSymbol implements Node {

    private final Symbol symbol;

    public NodeSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    @Override
    public void eval(Context context) {
        if (symbol.matches(context.tape.peek())) {
            context.tape.move();
        }
        else {
            throw new InvalidSyntaxException("Expected: " + symbol, context.tape.getLocation());
        }
    }

    @Override
    public boolean test(char value) {
        return symbol.matches(value);
    }

    @Override
    public Node collapse(NodeContext context) {
        return this;
    }

    @Override
    public Node compile(NodeContext context) {
        return this;
    }

    @Override
    public Node tryStack(Node other) {
        if (other instanceof NodeSymbol) {
            var ns = (NodeSymbol)other;

            if (this.symbol.stacks(ns.symbol)) {
                return this;
            }
        }

        return null;
    }

    @Override
    public List<NodeVertex> toVertices() {
        return List.of(new NodeVertex(this));
    }

    @Override
    public List<Node> getNodes() {
        return List.of();
    }

    @Override
    public boolean isOptional() {
        return false;
    }
}
