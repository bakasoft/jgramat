package gramat.pipeline.assembling;

import gramat.scheme.core.badges.Badge;
import gramat.scheme.models.expressions.ModelExpression;
import gramat.scheme.graph.Graph;
import gramat.scheme.graph.Node;
import gramat.scheme.graph.sets.NodeSetMutable;
import gramat.parsers.ValueParser;
import gramat.scheme.core.symbols.Alphabet;
import gramat.scheme.graph.sets.NodeSet;

import java.util.Set;

public interface BaseAssembler {

    void registerDependency(String reference);

    int nextTransactionID(ModelExpression expression);

    Badge getEmptyBadge();

    Alphabet getAlphabet();

    ValueParser findParser(String parser);

    ModelExpression findExpression(String name);

    Set<String> getRecursiveReferences();

    NodeSet compileExpression(Graph graph, Node source, ModelExpression expression);

    default NodeSet compileExpression(Graph graph, NodeSet sources, ModelExpression expression) {
        var targets = new NodeSetMutable();

        for (var initial : sources) {
            targets.addAll(compileExpression(graph, initial, expression));
        }

        return targets.build();
    }

}
