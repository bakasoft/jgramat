package gramat.pipeline.blueprint.builders;

import gramat.badges.Badge;
import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.graph.sets.NodeSetMutable;
import gramat.models.expressions.*;
import gramat.parsers.ValueParser;
import gramat.symbols.Alphabet;
import gramat.graph.sets.NodeSet;

import java.util.Set;

public interface BaseBuilder {

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
