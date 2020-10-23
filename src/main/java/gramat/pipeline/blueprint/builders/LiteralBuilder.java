package gramat.pipeline.blueprint.builders;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.ModelLiteral;
import gramat.graph.sets.NodeSet;

public interface LiteralBuilder extends BaseBuilder {

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
