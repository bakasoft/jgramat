package gramat.pipeline.assembling;

import gramat.scheme.graph.Graph;
import gramat.scheme.graph.Node;
import gramat.scheme.models.expressions.ModelOptional;
import gramat.scheme.graph.sets.NodeSet;

public interface OptionalAssembler extends BaseAssembler {

    default NodeSet compileOptional(Graph graph, Node source, ModelOptional optional) {
        return NodeSet.of(source, compileExpression(graph, source, optional.content));
    }

}
