package gramat.actions;

import gramat.eval.Context;
import gramat.eval.transactions.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NotBeginAction implements ActionTransaction {

    private final Transaction transaction;

    public NotBeginAction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public void run(Context context) {
        var badge = context.heap.peek();

        context.manager.notBegin(transaction.getID(), badge);
    }

    @Override
    public String toString() {
        return String.format("not-begin(%s)", transaction.getID());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotBeginAction that = (NotBeginAction) o;
        return transaction.getID() == that.transaction.getID();
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaction.getID());
    }
}
