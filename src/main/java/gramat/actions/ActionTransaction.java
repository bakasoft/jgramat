package gramat.actions;

import gramat.eval.transactions.Transaction;

public interface ActionTransaction extends Action {

    Transaction getTransaction();

}
