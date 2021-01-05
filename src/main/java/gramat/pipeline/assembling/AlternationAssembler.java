package gramat.pipeline.assembling;

import gramat.scheme.models.Graph;
import gramat.scheme.models.Node;
import gramat.scheme.models.sets.NodeSetMutable;
import gramat.scheme.data.expressions.AlternationData;
import gramat.scheme.models.sets.NodeSet;

public interface AlternationAssembler extends BaseAssembler {

    default NodeSet compileAlternation(Graph graph, Node source, AlternationData alternation) {
        var targets = new NodeSetMutable();

        for (var option : alternation.options) {
            targets.addAll(
                    compileExpression(graph, source, option)
            );
        }

        return targets.build();
    }

}
