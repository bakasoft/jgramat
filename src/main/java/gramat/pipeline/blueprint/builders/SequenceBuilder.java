package gramat.pipeline.blueprint.builders;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.ModelSequence;
import gramat.util.Chain;

public interface SequenceBuilder extends BaseBuilder {

    default Chain<Node> compileSequence(Graph graph, Node source, Node target, ModelSequence sequence) {
        Chain<Node> last = null;

        for (var i = 0; i < sequence.items.size(); i++) {
            Chain<Node> itemSource;
            Chain<Node> itemTarget;

            if (i == 0) {
                itemSource = Chain.of(source);
            }
            else {
                itemSource = last;
            }

            if (i == sequence.items.size() - 1) {
                // only for last item
                itemTarget = Chain.of(target);
            }
            else {
                itemTarget = Chain.of(graph.createNode());
            }

            last = compileExpression(graph, itemSource, itemTarget, sequence.items.get(i));
        }

        if (last == null) {
            return Chain.of(source, target);
        }

        return last;
    }

}
