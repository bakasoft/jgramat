package gramat.actions;

import gramat.eval.Context;
import gramat.eval.transactions.Transaction;

public class EndAction extends Action {

    private final Transaction transaction;

    public EndAction(Transaction transaction) {
        this.transaction = transaction;
    }


    @Override
    public void run(Context context) {
        var badge = context.heap.peek();

        context.manager.end(transaction, badge);
    }

    @Override
    public String toString() {
        return String.format("end(%s)", transaction.getID());
    }
}
