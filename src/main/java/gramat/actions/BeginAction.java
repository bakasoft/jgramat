package gramat.actions;

import gramat.actions.transactions.*;
import gramat.eval.transactions.Transaction;
import gramat.eval.Context;
import gramat.exceptions.UnsupportedValueException;

import java.util.Objects;

public class BeginAction implements Action {

    private final Transaction transaction;

    public BeginAction(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public void run(Context context) {
        var badge = context.heap.peek();

        context.manager.begin(badge, transaction);
    }

    @Override
    public String toString() {
        if (transaction instanceof ArrayTransaction) {
            return String.format("begin(array, %s)", transaction.getID());
        }
        else if (transaction instanceof AttributeTransaction) {
            return String.format("begin(attribute, %s)", transaction.getID());
        }
        else if (transaction instanceof NameTransaction) {
            return String.format("begin(name, %s)", transaction.getID());
        }
        else if (transaction instanceof ObjectTransaction) {
            return String.format("begin(object, %s)", transaction.getID());
        }
        else if (transaction instanceof ValueTransaction) {
            return String.format("begin(value, %s)", transaction.getID());
        }
        else {
            throw new UnsupportedValueException(transaction);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeginAction that = (BeginAction) o;
        return Objects.equals(transaction.getID(), that.transaction.getID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaction.getID());
    }
}