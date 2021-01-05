package gramat.pipeline.assembling;

import gramat.scheme.models.Graph;
import gramat.scheme.models.Node;
import gramat.scheme.data.expressions.OptionalData;
import gramat.scheme.models.sets.NodeSet;

public interface OptionalAssembler extends BaseAssembler {

    default NodeSet compileOptional(Graph graph, Node source, OptionalData optional) {
        return NodeSet.of(source, compileExpression(graph, source, optional.content));
    }

}
