package gramat.pipeline.expressions;

import gramat.exceptions.UnsupportedValueException;
import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.*;
import gramat.util.Chain;

public interface ExpressionFactory extends BaseFactory,
        AlternationFactory,
        LiteralFactory,
        OptionalFactory,
        RangeFactory,
        ReferenceFactory,
        RepetitionFactory,
        SequenceFactory,
        WildFactory,
        SpecialFactory {

    @Override
    default Chain<Node> compileExpression(Graph graph, Chain<Node> sources, Node target, ModelExpression expression) {
        var targets = Chain.of(target);

        for (var initial : sources) {
            targets = targets.merge(compileExpression(graph, initial, target, expression));
        }

        return targets;
    }

    @Override
    default Chain<Node> compileExpression(Graph graph, Node source, Chain<Node> targets, ModelExpression expression) {
        for (var accepted : targets) {
            targets = targets.merge(compileExpression(graph, source, accepted, expression));
        }

        return targets;
    }

    @Override
    default Chain<Node> compileExpression(Graph graph, Chain<Node> sources, Chain<Node> targets, ModelExpression expression) {
        for (var initial : sources) {
            for (var accepted : targets) {
                targets = targets.merge(compileExpression(graph, initial, accepted, expression));
            }
        }

        return targets;
    }

    @Override
    default Chain<Node> compileExpression(Graph graph, Node source, Node target, ModelExpression expression) {
        if (expression instanceof ModelLiteral) {
            return compileLiteral(graph, source, target, (ModelLiteral)expression);
        }
        else if (expression instanceof ModelOptional) {
            return compileOptional(graph, source, target, (ModelOptional)expression);
        }
        else if (expression instanceof ModelRepetition) {
            return compileRepetition(graph, source, target, (ModelRepetition)expression);
        }
        else if (expression instanceof ModelValue) {
            return compileValue(graph, source, target, (ModelValue)expression);
        }
        else if (expression instanceof ModelReference) {
            return compileReference(graph, source, target, (ModelReference)expression);
        }
        else if (expression instanceof ModelSequence) {
            return compileSequence(graph, source, target, (ModelSequence)expression);
        }
        else if (expression instanceof ModelAlternation) {
            return compileAlternation(graph, source, target, (ModelAlternation)expression);
        }
        else if (expression instanceof ModelWild) {
            return compileWild(graph, source, target, (ModelWild)expression);
        }
        else if (expression instanceof ModelArray) {
            return compileArray(graph, source, target, (ModelArray)expression);
        }
        else if (expression instanceof ModelObject) {
            return compileObject(graph, source, target, (ModelObject)expression);
        }
        else if (expression instanceof ModelAttribute) {
            return compileAttribute(graph, source, target, (ModelAttribute)expression);
        }
        else if (expression instanceof ModelName) {
            return compileName(graph, source, target, (ModelName)expression);
        }
        else if (expression instanceof ModelRange) {
            return compileRange(graph, source, target, (ModelRange)expression);
        }
        else {
            throw new UnsupportedValueException(expression);
        }
    }

}
