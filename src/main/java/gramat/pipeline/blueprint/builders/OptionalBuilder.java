package gramat.pipeline.blueprint.builders;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.ModelOptional;
import gramat.graph.sets.NodeSet;

public interface OptionalBuilder extends BaseBuilder {

    default NodeSet compileOptional(Graph graph, Node source, ModelOptional optional) {
        return NodeSet.of(source, compileExpression(graph, source, optional.content));
    }

}
