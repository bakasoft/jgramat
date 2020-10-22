package gramat.pipeline.blueprint.builders;

import gramat.badges.Badge;
import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.*;
import gramat.parsers.ValueParser;
import gramat.symbols.Alphabet;
import gramat.util.Chain;

import java.util.Set;

public interface BaseBuilder {

    void registerDependency(String reference);

    int nextTransactionID();

    Badge getEmptyBadge();

    Alphabet getAlphabet();

    ValueParser findParser(String parser);

    ModelExpression findExpression(String name);

    Set<String> getRecursiveReferences();

    Chain<Node> compileExpression(Graph graph, Node source, Node target, ModelExpression expression);

    default Chain<Node> compileExpression(Graph graph, Chain<Node> sources, Node target, ModelExpression expression) {
        var targets = Chain.of(target);

        for (var initial : sources) {
            targets = targets.merge(compileExpression(graph, initial, target, expression));
        }

        return targets;
    }

    default Chain<Node> compileExpression(Graph graph, Chain<Node> sources, Chain<Node> targets, ModelExpression expression) {
        for (var initial : sources) {
            for (var accepted : targets) {
                targets = targets.merge(compileExpression(graph, initial, accepted, expression));
            }
        }

        return targets;
    }

}
