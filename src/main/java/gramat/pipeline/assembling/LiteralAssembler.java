package gramat.pipeline.assembling;

import gramat.scheme.models.Graph;
import gramat.scheme.models.Node;
import gramat.scheme.data.expressions.LiteralData;
import gramat.scheme.models.sets.NodeSet;

public interface LiteralAssembler extends BaseAssembler {

    default NodeSet compileLiteral(Graph graph, Node source, LiteralData literal) {
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
