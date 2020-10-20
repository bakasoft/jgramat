package gramat.pipeline.expressions;

import gramat.actions.*;
import gramat.eval.transactions.Transaction;
import gramat.actions.transactions.*;
import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.graph.plugs.PlugType;
import gramat.models.expressions.*;
import gramat.util.Chain;

public interface SpecialFactory extends BaseFactory {

    default Chain<Node> compileArray(Graph graph, Node source, Node target, ModelArray array) {
        var id = nextTransactionID();
        var trx = new ArrayTransaction(id, array.type);
        return wrapActions(graph, source, target, array.content, trx);
    }

    default Chain<Node> compileAttribute(Graph graph, Node source, Node target, ModelAttribute attribute) {
        var id = nextTransactionID();
        var trx = new AttributeTransaction(id, attribute.name);
        return wrapActions(graph, source, target, attribute.content, trx);
    }

    default Chain<Node> compileName(Graph graph, Node source, Node target, ModelName name) {
        var id = nextTransactionID();
        var trx = new NameTransaction(id);
        return wrapActions(graph, source, target, name.content, trx);
    }

    default Chain<Node> compileObject(Graph graph, Node source, Node target, ModelObject object) {
        var id = nextTransactionID();
        var trx = new ObjectTransaction(id);
        return wrapActions(graph, source, target, object.content, trx);
    }

    default Chain<Node> compileValue(Graph graph, Node source, Node target, ModelValue value) {
        var parser = findParser(value.parser);
        var id = nextTransactionID();
        var trx = new ValueTransaction(id, parser);
        return wrapActions(graph, source, target, value.content, trx);
    }

    private Chain<Node> wrapActions(Graph graph, Node source, Node target, ModelExpression expression, Transaction transaction) {
        var targets = compileExpression(graph, source, target, expression);
        var begin = new BeginAction(transaction);
        var prevent = new PreventAction(transaction.getID());
        var cancel = new CancelAction(transaction.getID());
        var end = new EndAction(transaction);

        for (var link : graph.findLinksBetween(source, targets)) {
            // TODO what about Inner Node to Outside Node?
            switch(PlugType.compute(source, targets, link)) {
                case S2T:
                    link.event.wrap(begin, end);
                    break;
                case S2N:
                    link.event.prepend(begin);
                    break;
                case T2S:
                    link.event.prepend(prevent);
                    link.event.append(cancel);
                    break;
                case T2N:
                    link.event.append(cancel);
                    break;
                case N2S:
                    link.event.prepend(prevent);
                    break;
                case N2T:
                    link.event.append(end);
                    break;
                default:
                    break;
            }
        }

        return targets;
    }

}
