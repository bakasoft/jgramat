package gramat.pipeline.expressions;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.ModelOptional;
import gramat.util.Chain;

public interface OptionalFactory extends BaseFactory {

    default Chain<Node> compileOptional(Graph graph, Node source, Node target, ModelOptional optional) {
        var contentAccepted = compileExpression(graph, source, target, optional.content);

        return contentAccepted.merge(source);
    }

}
