package gramat.pipeline.assembling;

import gramat.scheme.graph.Graph;
import gramat.scheme.graph.Node;
import gramat.scheme.models.expressions.ModelLiteral;
import gramat.scheme.graph.sets.NodeSet;

public interface LiteralAssembler extends BaseAssembler {

    default NodeSet compileLiteral(Graph graph, Node source, ModelLiteral literal) {
        var badge = getEmptyBadge();
        Node last = source;

        for (var chr : literal.value.toCharArray()) {
            var current = graph.createNode();
            var symbol = getAlphabet().character(chr);

            graph.createLink(last, current, symbol, badge);

            last = current;
        }

        return NodeSet.of(last);
    }

}
