package gramat.pipeline.expressions;

import gramat.actions.*;
import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.graph.plugs.PlugType;
import gramat.models.expressions.*;
import gramat.util.Chain;

public interface SpecialFactory extends BaseFactory {

    default Chain<Node> compileArray(Graph graph, Node source, Node target, ModelArray array) {
        var trxID = nextTransactionID();
        var begin = new ArrayBegin(trxID);
        var end = new ArrayEnd(trxID, array.type);
        return wrapActions(graph, source, target, array.content, begin, end);
    }

    default Chain<Node> compileAttribute(Graph graph, Node source, Node target, ModelAttribute attribute) {
        var trxID = nextTransactionID();
        var begin = new AttributeBegin(trxID, attribute.name);
        var end = new AttributeEnd(trxID, attribute.name);
        return wrapActions(graph, source, target, attribute.content, begin, end);
    }

    default Chain<Node> compileName(Graph graph, Node source, Node target, ModelName name) {
        var trxID = nextTransactionID();
        var begin = new NameBegin(trxID);
        var end = new NameEnd(trxID);
        return wrapActions(graph, source, target, name.content, begin, end);
    }

    default Chain<Node> compileObject(Graph graph, Node source, Node target, ModelObject object) {
        var trxID = nextTransactionID();
        var begin = new ObjectBegin(trxID);
        var end = new ObjectEnd(trxID, object.type);
        return wrapActions(graph, source, target, object.content, begin, end);
    }

    default Chain<Node> compileValue(Graph graph, Node source, Node target, ModelValue value) {
        var parser = findParser(value.parser);
        var trxID = nextTransactionID();
        var begin = new ValueBegin(trxID);
        var end = new ValueEnd(trxID, parser);
        return wrapActions(graph, source, target, value.content, begin, end);
    }

    private Chain<Node> wrapActions(Graph graph, Node source, Node target, ModelExpression expression, Action beforeAction, Action afterAction) {
        var targets = compileExpression(graph, source, target, expression);

        for (var link : graph.findLinksBetween(source, targets)) {
            switch(PlugType.compute(source, targets, link)) {
                case S2N:
                    link.eventPrepend(beforeAction);
                    break;
                case S2T:
                    link.eventWrap(beforeAction, afterAction);
                    break;
                case N2T:
                    link.eventAppend(afterAction);
                    break;
                default:
                    break;
            }
        }

        return targets;
    }

}
