package gramat.eval.trx;

public class Transaction {

    final int id;
    final Runnable commitAction;

    TransactionStatus status;

    Transaction(int id, Runnable commitAction, TransactionStatus status) {
        this.id = id;
        this.commitAction = commitAction;
        this.status = status;
    }

}
