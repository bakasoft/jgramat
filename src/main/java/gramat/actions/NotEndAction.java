package gramat.actions;

import gramat.eval.EvalContext;
import gramat.eval.transactions.Transaction;

import java.util.Objects;

public class NotEndAction implements ActionTransaction {

    private final Transaction transaction;

    public NotEndAction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public void run(EvalContext context) {
        var badge = context.heap.peek();

        context.manager.notEnd(transaction, badge);
    }

    @Override
    public String toString() {
        return String.format("not-end(%s)", transaction.getID());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotEndAction that = (NotEndAction) o;
        return Objects.equals(transaction.getID(), that.transaction.getID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaction.getID());
    }
}
