package gramat.pipeline.assembling;

import gramat.scheme.common.badges.Badge;
import gramat.scheme.data.expressions.ExpressionData;
import gramat.scheme.models.Graph;
import gramat.scheme.models.Node;
import gramat.scheme.models.sets.NodeSetMutable;
import gramat.scheme.common.parsers.ValueParser;
import gramat.scheme.common.symbols.Alphabet;
import gramat.scheme.models.sets.NodeSet;

import java.util.Set;

public interface BaseAssembler {

    void registerDependency(String reference);

    int nextTransactionID(ExpressionData expression);

    Badge getEmptyBadge();

    Alphabet getAlphabet();

    ValueParser findParser(String parser);

    ExpressionData findExpression(String name);

    Set<String> getRecursiveReferences();

    NodeSet compileExpression(Graph graph, Node source, ExpressionData expression);

    default NodeSet compileExpression(Graph graph, NodeSet sources, ExpressionData expression) {
        var targets = new NodeSetMutable();

        for (var initial : sources) {
            targets.addAll(compileExpression(graph, initial, expression));
        }

        return targets.build();
    }

}
