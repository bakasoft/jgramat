package gramat.eval.transactions;

import gramat.badges.Badge;
import gramat.eval.Context;

import java.util.*;

public class TransactionManager {

    private final Context context;
    private final Set<Integer> skip;

    private Queue<TRX> pending;
    private Queue<TRX> complete;

    public TransactionManager(Context context) {
        this.context = context;
        pending = new LinkedList<>();
        complete = new LinkedList<>();
        skip = new HashSet<>();
    }

    public void begin(Badge badge, Transaction transaction) {
        var hash = computeHash(transaction.getID(), badge);

        if (skip.remove(hash)) {
            context.logger.debug("Skipping transaction: " + hash);
            return;
        }

        transaction.begin(context);
    }

    public void prevent(int id, Badge badge) {
        var hash = computeHash(id, badge);

        if (!skip.add(hash)) {
            throw new RuntimeException("Action2 was already ignored.");
        }
    }

    public void cancel(int id, Badge badge) {
        var hash = computeHash(id, badge);

        if (!complete.removeIf(trx -> trx.hash == hash)) {
            throw new RuntimeException();
        }
    }

    public void end(Transaction transaction, Badge badge) {
        var hash = computeHash(transaction.getID(), badge);

        pending.add(new TRX(hash, transaction.prepareCommit(context)));
    }

    public void commit() {
        var ready = complete;
        complete = pending;
        pending = new LinkedList<>();

        while (!ready.isEmpty()) {
            var trx = ready.remove();

            trx.action.run();
        }
    }

    public void flush() {
        commit();

        if (!complete.isEmpty() || !pending.isEmpty()) {
            throw new RuntimeException();
        }
    }

    private Integer computeHash(int id, Badge badge) {
        return Objects.hash(id, badge.hashCode());
    }

    private static class TRX {
        public final int hash;
        public final Runnable action;
        private TRX(int hash, Runnable action) {
            this.hash = hash;
            this.action = action;
        }
    }

}
