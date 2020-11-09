package gramat.pipeline.assembling;

import gramat.scheme.graph.Graph;
import gramat.scheme.graph.Node;
import gramat.scheme.models.expressions.ModelExpression;
import gramat.scheme.models.expressions.ModelRepetition;
import gramat.scheme.graph.sets.NodeSet;

public interface RepetitionAssembler extends BaseAssembler {

    default NodeSet compileRepetition(Graph graph, Node source, ModelRepetition repetition) {
        if (repetition.separator != null) {
            if (repetition.minimum == 0) {
                return compileZeroOrMany(graph, source, repetition.content, repetition.separator);
            }
            else if (repetition.minimum == 1) {
                return compileOneOrMany(graph, source, repetition.content, repetition.separator);
            }
        }
        else if (repetition.minimum == 0) {
            return compileZeroOrMany(graph, source, repetition.content);
        }
        else if (repetition.minimum == 1) {
            return compileOneOrMany(graph, source, repetition.content);
        }

        throw new RuntimeException();
    }

    private NodeSet compileZeroOrMany(Graph graph, Node source, ModelExpression content) {
        var accepted = graph.createNode();

        graph.mergeNodesInto(
                compileExpression(graph, source, content), accepted);

        graph.mergeNodesInto(
                compileExpression(graph, accepted, content), accepted);

        return NodeSet.of(source, accepted);
    }

    private NodeSet compileZeroOrMany(Graph graph, Node source, ModelExpression content, ModelExpression separator) {
        var accepted = graph.createNode();

        graph.mergeNodesInto(
                compileExpression(graph, source, content),
                accepted);

        var acceptedSeparator = compileExpression(graph, accepted, separator);

        graph.mergeNodesInto(
                compileExpression(graph, acceptedSeparator, content),
                accepted);

        return NodeSet.of(source, accepted);
    }

    private NodeSet compileOneOrMany(Graph graph, Node source, ModelExpression content) {
        var accepted = graph.createNode();

        graph.mergeNodesInto(
                compileExpression(graph, source, content),
                accepted
        );

        graph.mergeNodesInto(
                compileExpression(graph, accepted, content),
                accepted
        );

        return NodeSet.of(accepted);
    }

    private NodeSet compileOneOrMany(Graph graph, Node source, ModelExpression content, ModelExpression separator) {
        var accepted = graph.createNode();

        graph.mergeNodesInto(
                compileExpression(graph, source, content),
                accepted);

        var acceptedSeparator = compileExpression(graph, accepted, separator);

        graph.mergeNodesInto(
                compileExpression(graph, acceptedSeparator, content),
                accepted);

        return NodeSet.of(accepted);
    }

}
