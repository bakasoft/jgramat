package gramat.pipeline.assembling;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.ModelWild;
import gramat.graph.sets.NodeSet;

public interface WildAssembler extends BaseAssembler {

    default NodeSet compileWild(Graph graph, Node source, ModelWild wild) {
        source.wild = true;
        return NodeSet.of(source);
    }

}
