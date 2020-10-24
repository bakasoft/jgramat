package gramat.actions.transactions;

import gramat.eval.Context;
import gramat.eval.transactions.DefaultTransaction;

public class NameTransaction extends DefaultTransaction {

    public NameTransaction(int id) {
        super(id);
    }

    @Override
    public void begin(Context context) {
        context.pushContainer();
    }

    @Override
    public Runnable prepareCommit(Context context) {
        return () -> {
            var name = context.popValue();

            context.setName(String.valueOf(name));
        };
    }

    @Override
    public String getName() {
        return "name";
    }

}
