package gramat.pipeline.blueprint.builders;

import gramat.actions.*;
import gramat.eval.transactions.Transaction;
import gramat.actions.transactions.*;
import gramat.exceptions.UnsupportedValueException;
import gramat.graph.Graph;
import gramat.graph.Node;
import gramat.graph.util.DirType;
import gramat.models.expressions.*;
import gramat.util.Chain;

import java.util.ArrayList;

public interface SpecialBuilder extends BaseBuilder {

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
        graph.capturings.push(new ArrayList<>());

        var targets = compileExpression(graph, source, target, expression);

        var links = graph.capturings.pop();

        var begin = new BeginAction(transaction);
        var notBegin = new NotBeginAction(transaction.getID());
        var notEnd = new NotEndAction(transaction);
        var end = new EndAction(transaction);

        for (var link : links) {
            // TODO what about Inner Node to Outside Node?
            for (var dir : DirType.compute(source, targets, link)) {
                switch (dir) {
                    case FROM_SOURCE:
                        link.event.prepend(begin);
                        break;
                    case TO_TARGET:
                        link.event.append(end);
                        break;
                    case FROM_TARGET:
                        link.event.prepend(notEnd);
                        break;
                    case TO_SOURCE:
                        link.event.append(notBegin);
                        break;
                    default:
                        throw new UnsupportedValueException(dir);
                }
            }
        }

        return targets;
    }

}
