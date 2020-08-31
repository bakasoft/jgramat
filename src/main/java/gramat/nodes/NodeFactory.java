package gramat.nodes;

import gramat.nodes.impl.*;
import gramat.symbols.Symbol;
import gramat.symbols.SymbolLiteral;

import java.util.List;

public class NodeFactory {

    public Node alternation(Node... nodes) {
        return new NodeAlternation(List.of(nodes));
    }

    public NodeSequence sequence() {
        return new NodeSequence();
    }

    public NodeSequence sequence(Node... nodes) {
        return new NodeSequence(List.of(nodes));
    }

    public NodeSymbol symbol(Symbol symbol) {
        return new NodeSymbol(symbol);
    }

    public Node optional(Node node) {
        return new NodeOptional(node);
    }

    public Node repeat(Node node) {
        return new NodeRepeat(node);
    }

    public Node exact(Node node, int count) {
        var sequence = sequence();

        for (int i = 0; i < count; i++) {
            sequence.append(node);
        }

        return sequence;
    }

    public Node atLeast(int count, Node node) {
        var sequence = sequence();

        for (int i = 0; i < count; i++) {
            sequence.append(node);
        }

        sequence.append(repeat(node));

        return sequence;
    }

    public Node token(String token) {
        var sequence = sequence();

        for (var chr : token.toCharArray()) {
            sequence.append(symbol(new SymbolLiteral(chr)));
        }

        return sequence;
    }
}
