package gramat.pipeline.expressions;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.ModelAlternation;
import gramat.util.Chain;

public interface AlternationFactory extends BaseFactory {

    default Chain<Node> compileAlternation(Graph graph, Node source, Node target, ModelAlternation alternation) {
        var targets = Chain.of(target);

        for (var option : alternation.options) {
            var optionAccepted = compileExpression(graph, source, target, option);

            targets = targets.merge(optionAccepted);
        }

        return targets;
    }

}
