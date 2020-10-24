package gramat.actions;

import gramat.eval.Context;
import gramat.eval.transactions.Transaction;

import java.util.List;
import java.util.Objects;

public class EndAction implements Action {

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
    public String getName() {
        return "end";
    }

    @Override
    public List<String> getArguments() {
        return List.of(transaction.getName(), String.valueOf(transaction.getID()));
    }

    @Override
    public String toString() {
        return String.format("end(%s)", transaction.getID());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EndAction endAction = (EndAction) o;
        return Objects.equals(transaction.getID(), endAction.transaction.getID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaction.getID());
    }
}
