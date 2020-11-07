package gramat.eval.transactions;

import gramat.eval.EvalContext;

import java.util.Map;

public interface Transaction {

    int getID();

    void begin(EvalContext context);

    Runnable prepareCommit(EvalContext context);

    Map<String, Object> getArgs();

}
