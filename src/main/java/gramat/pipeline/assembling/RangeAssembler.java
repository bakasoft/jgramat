package gramat.pipeline.assembling;

import gramat.scheme.models.Graph;
import gramat.scheme.models.Node;
import gramat.scheme.data.expressions.RangeData;
import gramat.scheme.models.sets.NodeSet;

public interface RangeAssembler extends BaseAssembler {

    default NodeSet compileRange(Graph graph, Node source, RangeData range) {
        var symbol = getAlphabet().range(range.begin, range.end);
        var target = graph.createNode();

        graph.createLink(source, target, symbol, getEmptyBadge());

        return NodeSet.of(target);
    }

}
