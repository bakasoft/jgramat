package gramat.pipeline.assembling;

import gramat.scheme.models.Graph;
import gramat.scheme.models.Node;
import gramat.scheme.data.expressions.WildData;
import gramat.scheme.models.sets.NodeSet;

public interface WildAssembler extends BaseAssembler {

    default NodeSet compileWild(Graph graph, Node source, WildData wild) {
        source.wild = true;
        return NodeSet.of(source);
    }

}
