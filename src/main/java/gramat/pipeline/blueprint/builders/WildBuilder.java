package gramat.pipeline.blueprint.builders;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.ModelWild;
import gramat.util.Chain;

public interface WildBuilder extends BaseBuilder {

    default Chain<Node> compileWild(Graph graph, Node source, Node target, ModelWild wild) {
        source.wild = true;
        target.wild = true;
        return Chain.of(source, target);
    }

}
