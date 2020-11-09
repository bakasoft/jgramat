package gramat.pipeline.assembling;

import gramat.scheme.graph.Graph;
import gramat.scheme.graph.Node;
import gramat.scheme.models.expressions.ModelWild;
import gramat.scheme.graph.sets.NodeSet;

public interface WildAssembler extends BaseAssembler {

    default NodeSet compileWild(Graph graph, Node source, ModelWild wild) {
        source.wild = true;
        return NodeSet.of(source);
    }

}
