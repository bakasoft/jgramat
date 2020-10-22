package gramat.pipeline.blueprint.builders;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.ModelReference;
import gramat.util.Chain;

public interface ReferenceBuilder extends BaseBuilder {

    default Chain<Node> compileReference(Graph graph, Node source, Node target, ModelReference reference) {
        if (getRecursiveReferences().contains(reference.name)) {
            var symbol = getAlphabet().reference(reference.name);
            var badge = getEmptyBadge();

            graph.createLink(source, target, symbol, badge);

            registerDependency(reference.name);

            return Chain.of(target);
        }
        else {
            var expression = findExpression(reference.name);

            return compileExpression(graph, source, target, expression);
        }
    }

}
