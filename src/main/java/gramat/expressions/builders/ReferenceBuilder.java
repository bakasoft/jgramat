package gramat.expressions.builders;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.expressions.models.ModelReference;
import gramat.graph.sets.NodeSet;

public interface ReferenceBuilder extends BaseBuilder {

    default NodeSet compileReference(Graph graph, Node source, ModelReference reference) {
        if (getRecursiveReferences().contains(reference.name)) {
            var symbol = getAlphabet().reference(reference.name);
            var badge = getEmptyBadge();
            var target = graph.createNode();

            graph.createLink(source, target, symbol, badge);

            registerDependency(reference.name);

            return NodeSet.of(target);
        }
        else {
            var expression = findExpression(reference.name);

            return compileExpression(graph, source, expression);
        }
    }

}
