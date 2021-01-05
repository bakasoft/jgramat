package gramat.pipeline.assembling;

import gramat.scheme.common.actions.*;
import gramat.eval.transactions.Transaction;
import gramat.scheme.common.actions.transactions.*;
import gramat.exceptions.UnsupportedValueException;
import gramat.scheme.models.Graph;
import gramat.scheme.models.Node;
import gramat.scheme.models.util.DirType;
import gramat.scheme.models.sets.NodeSet;
import gramat.scheme.data.expressions.*;

import java.util.ArrayList;

public interface SpecialAssembler extends BaseAssembler {

    default NodeSet compileArray(Graph graph, Node source, ArrayData array) {
        var id = nextTransactionID(array);
        var trx = new ArrayTransaction(id, array.type);
        return wrapActions(graph, source, array.content, trx);
    }

    default NodeSet compileAttribute(Graph graph, Node source, AttributeData attribute) {
        var id = nextTransactionID(attribute);
        var trx = new AttributeTransaction(id, attribute.name);
        return wrapActions(graph, source, attribute.content, trx);
    }

    default NodeSet compileName(Graph graph, Node source, NameData name) {
        var id = nextTransactionID(name);
        var trx = new NameTransaction(id);
        return wrapActions(graph, source, name.content, trx);
    }

    default NodeSet compileObject(Graph graph, Node source, ObjectData object) {
        var id = nextTransactionID(object);
        var trx = new ObjectTransaction(id, object.type);
        return wrapActions(graph, source, object.content, trx);
    }

    default NodeSet compileValue(Graph graph, Node source, ValueData value) {
        var parser = findParser(value.parser);
        var id = nextTransactionID(value);
        var trx = new ValueTransaction(id, parser);
        return wrapActions(graph, source, value.content, trx);
    }

    private NodeSet wrapActions(Graph graph, Node source, ExpressionData expression, Transaction transaction) {
        graph.capturings.push(new ArrayList<>());

        var targets = compileExpression(graph, source, expression);

        var links = graph.capturings.pop();

        var begin = new BeginAction(transaction);
        var notBegin = new NotBeginAction(transaction);
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
