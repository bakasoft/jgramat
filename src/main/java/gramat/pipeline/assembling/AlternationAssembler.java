package gramat.pipeline.assembling;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.graph.sets.NodeSetMutable;
import gramat.models.expressions.ModelAlternation;
import gramat.graph.sets.NodeSet;

public interface AlternationAssembler extends BaseAssembler {

    default NodeSet compileAlternation(Graph graph, Node source, ModelAlternation alternation) {
        var targets = new NodeSetMutable();

        for (var option : alternation.options) {
            targets.addAll(
                    compileExpression(graph, source, option)
            );
        }

        return targets.build();
    }

}
