package gramat.pipeline.assembling;

import gramat.scheme.models.Graph;
import gramat.scheme.models.Node;
import gramat.scheme.data.expressions.SequenceData;
import gramat.scheme.models.sets.NodeSet;

public interface SequenceAssembler extends BaseAssembler {

    default NodeSet compileSequence(Graph graph, Node source, SequenceData sequence) {
        var last = NodeSet.of(source);

        for (var i = 0; i < sequence.items.size(); i++) {
            last = compileExpression(graph, last, sequence.items.get(i));
        }

        return last;
    }

}
