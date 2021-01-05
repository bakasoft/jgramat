package gramat.pipeline.assembling;

import gramat.scheme.models.Graph;
import gramat.scheme.models.Node;
import gramat.scheme.data.expressions.ReferenceData;
import gramat.scheme.models.sets.NodeSet;

public interface ReferenceAssembler extends BaseAssembler {

    default NodeSet compileReference(Graph graph, Node source, ReferenceData reference) {
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
