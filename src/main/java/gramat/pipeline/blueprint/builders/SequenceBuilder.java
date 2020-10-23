package gramat.pipeline.blueprint.builders;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.ModelSequence;
import gramat.graph.sets.NodeSet;

public interface SequenceBuilder extends BaseBuilder {

    default NodeSet compileSequence(Graph graph, Node source, ModelSequence sequence) {
        var last = NodeSet.of(source);

        for (var i = 0; i < sequence.items.size(); i++) {
            last = compileExpression(graph, last, sequence.items.get(i));
        }

        return last;
    }

}
