package gramat.scheme.common.actions;

import gramat.eval.transactions.Transaction;

public interface ActionTransaction extends Action {

    Transaction getTransaction();

}
