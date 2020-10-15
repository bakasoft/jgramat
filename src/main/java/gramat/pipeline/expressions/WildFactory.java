package gramat.pipeline.expressions;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.ModelWild;
import gramat.util.Chain;

public interface WildFactory extends BaseFactory {

    default Chain<Node> compileWild(Graph graph, Node source, Node target, ModelWild wild) {
        source.wild = true;
        target.wild = true;
        return Chain.of(source, target);
    }

}
