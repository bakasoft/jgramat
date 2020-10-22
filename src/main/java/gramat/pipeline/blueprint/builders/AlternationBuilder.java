package gramat.pipeline.blueprint.builders;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.ModelAlternation;
import gramat.util.Chain;

public interface AlternationBuilder extends BaseBuilder {

    default Chain<Node> compileAlternation(Graph graph, Node source, Node target, ModelAlternation alternation) {
        var targets = Chain.of(target);

        for (var option : alternation.options) {
            var optionAccepted = compileExpression(graph, source, target, option);

            targets = targets.merge(optionAccepted);
        }

        return targets;
    }

}
