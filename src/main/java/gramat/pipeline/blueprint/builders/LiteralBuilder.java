package gramat.pipeline.blueprint.builders;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.ModelLiteral;
import gramat.util.Chain;

public interface LiteralBuilder extends BaseBuilder {

    default Chain<Node> compileLiteral(Graph graph, Node source, Node target, ModelLiteral literal) {
        var chars = literal.value.toCharArray();
        var badge = getEmptyBadge();
        Node last = null;

        for (var i = 0; i < chars.length; i++) {
            Node itemSource;
            Node itemTarget;

            if (i == 0) {
                itemSource = source;
            }
            else {
                itemSource = last;
            }

            if (i == chars.length - 1) {
                // only for last item
                itemTarget = target;
            }
            else {
                itemTarget = graph.createNode();
            }

            var symbol = getAlphabet().character(chars[i]);

            graph.createLink(itemSource, itemTarget, symbol, badge);

            last = itemTarget;
        }

        if (last == null) {
            return Chain.of(source, target);
        }

        return Chain.of(last);
    }

}
