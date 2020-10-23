package gramat.pipeline.blueprint.builders;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.ModelRange;
import gramat.graph.sets.NodeSet;

public interface RangeBuilder extends BaseBuilder {

    default NodeSet compileRange(Graph graph, Node source, ModelRange range) {
        var symbol = getAlphabet().range(range.begin, range.end);
        var target = graph.createNode();

        graph.createLink(source, target, symbol, getEmptyBadge());

        return NodeSet.of(target);
    }

}
