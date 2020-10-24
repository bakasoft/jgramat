package gramat.eval.transactions;

import gramat.eval.Context;

public interface Transaction {

    int getID();

    void begin(Context context);

    Runnable prepareCommit(Context context);

    String getName();

}
