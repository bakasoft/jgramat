package gramat.eval.trx;

import gramat.framework.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransactionManager {

    public final String token;

    private final Logger logger;
    private final Map<TransactionID, Transaction> transactions;

    public TransactionManager(Logger logger, String token) {
        this.logger = logger;
        this.token = token;
        transactions = new HashMap<>();
    }

    public boolean contains(TransactionID id) {
        return transactions.containsKey(id);
    }

    public void begin(TransactionID id, Runnable commitAction) {
        logger.debug("begin trx %s", id);
        var trx = new Transaction(id, commitAction, TransactionStatus.BEGIN);

        if (transactions.put(id, trx) != null) {
            throw new RuntimeException();
        }
    }

    public void keep(TransactionID id) {
        logger.debug("continue trx %s", id);
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

    public void commit(TransactionID id) {
        logger.debug("mark commit trx %s", id);
        var trx = transactions.get(id);

        if (trx == null) {
            throw new RuntimeException("transaction not found: " + id);
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
        for (var trx : new ArrayList<>(transactions.values())) {
            if (trx.status == TransactionStatus.BEGIN) {
                logger.debug("mark rollback trx: %s", trx.id);
                trx.status = TransactionStatus.ROLLBACK;
            }
            else if (trx.status == TransactionStatus.ROLLBACK) {
                logger.debug("rollback trx: %s", trx.id);
                transactions.remove(trx.id);
            }
            else if (trx.status == TransactionStatus.COMMIT) {
                logger.debug("commit trx: %s", trx.id);

                trx.commitAction.run();

                transactions.remove(trx.id);
            }
            else {
                throw new RuntimeException();
            }
        }
    }
}
