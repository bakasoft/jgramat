package gramat.pipeline.assembling;

import gramat.scheme.graph.Graph;
import gramat.scheme.graph.Node;
import gramat.scheme.models.expressions.ModelRange;
import gramat.scheme.graph.sets.NodeSet;

public interface RangeAssembler extends BaseAssembler {

    default NodeSet compileRange(Graph graph, Node source, ModelRange range) {
        var symbol = getAlphabet().range(range.begin, range.end);
        var target = graph.createNode();

        graph.createLink(source, target, symbol, getEmptyBadge());

        return NodeSet.of(target);
    }

}
