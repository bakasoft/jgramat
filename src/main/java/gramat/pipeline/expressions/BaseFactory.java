package gramat.pipeline.expressions;

import gramat.actions.Action;
import gramat.badges.Badge;
import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.models.expressions.*;
import gramat.parsers.ValueParser;
import gramat.symbols.Alphabet;
import gramat.symbols.Symbol;
import gramat.util.Chain;

public interface BaseFactory {

    void registerDependency(String reference);

    int nextTransactionID();

    Badge getEmptyBadge();

    Alphabet getAlphabet();

    ValueParser findParser(String parser);

    Chain<Node> compileExpression(Graph graph, Chain<Node> sources, Node target, ModelExpression expression);

    Chain<Node> compileExpression(Graph graph, Node source, Chain<Node> targets, ModelExpression expression);

    Chain<Node> compileExpression(Graph graph, Chain<Node> sources, Chain<Node> targets, ModelExpression expression);

    Chain<Node> compileExpression(Graph graph, Node source, Node target, ModelExpression expression);

}
