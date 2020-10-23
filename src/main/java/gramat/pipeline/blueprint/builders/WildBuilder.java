package gramat.pipeline.blueprint.builders;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.ModelWild;
import gramat.graph.sets.NodeSet;

public interface WildBuilder extends BaseBuilder {

    default NodeSet compileWild(Graph graph, Node source, ModelWild wild) {
        source.wild = true;
        return NodeSet.of(source);
    }

}
