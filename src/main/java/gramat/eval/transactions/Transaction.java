package gramat.eval.transactions;

import gramat.eval.Context;

import java.util.Map;

public interface Transaction {

    int getID();

    void begin(Context context);

    Runnable prepareCommit(Context context);

    Map<String, Object> getArgs();

}
