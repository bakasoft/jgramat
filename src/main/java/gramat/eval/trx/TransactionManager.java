package gramat.eval.trx;

import java.util.HashMap;
import java.util.Map;

public class TransactionManager {

    private final Map<Integer, Transaction> transactions;

    public TransactionManager() {
        transactions = new HashMap<>();
    }

    public boolean contains(int id) {
        return transactions.containsKey(id);
    }

    public void begin(int id, Runnable commitAction) {
        var trx = new Transaction(id, commitAction, TransactionStatus.BEGIN);

        if (transactions.put(id, trx) != null) {
            throw new RuntimeException();
        }
    }

    public void keep(int id) {
        var trx = transactions.get(id);

        if (trx == null) {
            throw new RuntimeException();
        }

        if (trx.status == TransactionStatus.BEGIN) {
            throw new RuntimeException();
        }
        else if (trx.status == TransactionStatus.ROLLBACK) {
            trx.status = TransactionStatus.BEGIN;
        }
        else if (trx.status == TransactionStatus.COMMIT) {
            throw new RuntimeException();
        }
        else {
            throw new RuntimeException();
        }
    }

    public void commit(int id) {
        var trx = transactions.get(id);

        if (trx == null) {
            throw new RuntimeException();
        }

        if (trx.status == TransactionStatus.BEGIN) {
            trx.status = TransactionStatus.COMMIT;
        }
        else if (trx.status == TransactionStatus.ROLLBACK) {
            throw new RuntimeException();
        }
        else if (trx.status == TransactionStatus.COMMIT) {
            throw new RuntimeException();
        }
        else {
            throw new RuntimeException();
        }
    }

    public void flush() {
        for (var trx : transactions.values()) {
            if (trx.status == TransactionStatus.BEGIN) {
                trx.status = TransactionStatus.ROLLBACK;
            }
            else if (trx.status == TransactionStatus.ROLLBACK) {
                // TODO rollback action?
                transactions.remove(trx.id);
            }
            else if (trx.status == TransactionStatus.COMMIT) {
                trx.commitAction.run();

                transactions.remove(trx.id);
            }
            else {
                throw new RuntimeException();
            }
        }
    }
}
