package gramat.actions;

import gramat.eval.Context;
import gramat.eval.transactions.Transaction;

public class NotEndAction extends Action {

    private final Transaction transaction;

    public NotEndAction(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public void run(Context context) {
        var badge = context.heap.peek();

        context.manager.notEnd(transaction, badge);
    }

    @Override
    public String toString() {
        return String.format("not-end(%s)", transaction.getID());
    }

}
