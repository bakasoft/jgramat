package gramat.pipeline.assembling;

import gramat.scheme.graph.Graph;
import gramat.scheme.graph.Node;
import gramat.scheme.models.expressions.ModelSequence;
import gramat.scheme.graph.sets.NodeSet;

public interface SequenceAssembler extends BaseAssembler {

    default NodeSet compileSequence(Graph graph, Node source, ModelSequence sequence) {
        var last = NodeSet.of(source);

        for (var i = 0; i < sequence.items.size(); i++) {
            last = compileExpression(graph, last, sequence.items.get(i));
        }

        return last;
    }

}
