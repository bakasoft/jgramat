package gramat.pipeline.blueprint.builders;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.ModelRange;
import gramat.util.Chain;

public interface RangeBuilder extends BaseBuilder {

    default Chain<Node> compileRange(Graph graph, Node source, Node target, ModelRange range) {
        var symbol = getAlphabet().range(range.begin, range.end);

        graph.createLink(source, target, symbol, getEmptyBadge());

        return Chain.of(target);
    }

}
