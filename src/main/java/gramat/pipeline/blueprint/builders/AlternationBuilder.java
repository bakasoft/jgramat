package gramat.pipeline.blueprint.builders;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.graph.sets.NodeSetMutable;
import gramat.models.expressions.ModelAlternation;
import gramat.graph.sets.NodeSet;

public interface AlternationBuilder extends BaseBuilder {

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
