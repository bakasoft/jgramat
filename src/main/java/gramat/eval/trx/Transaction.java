package gramat.eval.trx;

public class Transaction {

    final TransactionID id;
    final Runnable commitAction;

    TransactionStatus status;

    Transaction(TransactionID id, Runnable commitAction, TransactionStatus status) {
        this.id = id;
        this.commitAction = commitAction;
        this.status = status;
    }

}
