package gramat.pipeline.expressions;

import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.ModelRepetition;
import gramat.util.Chain;

public interface RepetitionFactory extends BaseFactory {

    default Chain<Node> compileRepetition(Graph graph, Node source, Node target, ModelRepetition repetition) {
        if (repetition.separator == null && repetition.minimum == 0) {
            var acceptedOne = compileExpression(graph, source, target, repetition.content);
            var acceptedBack = compileExpression(graph, acceptedOne, source, repetition.content);
            return acceptedOne.merge(acceptedBack);
        }
        else if (repetition.separator == null && repetition.minimum == 1) {
            var acceptedOne = compileExpression(graph, source, target, repetition.content);
            var acceptedLoop = compileExpression(graph, acceptedOne, acceptedOne, repetition.content);
            return acceptedOne.merge(acceptedLoop);
        }
        else if (repetition.separator != null && repetition.minimum == 0) {
            var acceptedOne = compileExpression(graph, source, target, repetition.content);
            var acceptedSep = compileExpression(graph, acceptedOne, graph.createNode(), repetition.separator);
            var acceptedLoop = compileExpression(graph, acceptedSep, acceptedOne, repetition.content);
            return acceptedOne.merge(acceptedLoop).merge(source);
        }
        else if (repetition.separator != null && repetition.minimum == 1) {
            var acceptedOne = compileExpression(graph, source, target, repetition.content);
            var acceptedSep = compileExpression(graph, acceptedOne, graph.createNode(), repetition.separator);
            var acceptedLoop = compileExpression(graph, acceptedSep, acceptedOne, repetition.content);
            return acceptedOne.merge(acceptedLoop);
        }
        else {
            throw new RuntimeException();
        }
    }

}
