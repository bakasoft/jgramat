package gramat.pipeline.assembling;

import gramat.scheme.graph.Graph;
import gramat.scheme.graph.Node;
import gramat.scheme.graph.sets.NodeSetMutable;
import gramat.scheme.models.expressions.ModelAlternation;
import gramat.scheme.graph.sets.NodeSet;

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
