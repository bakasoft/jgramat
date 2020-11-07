package gramat.actions.transactions;

import gramat.eval.Context;
import gramat.eval.transactions.TransactionAdapter;
import gramat.util.NameMap;

public class NameTransaction extends TransactionAdapter {

    public static final String NAME = "name";

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
    protected void fillArgs(NameMap<Object> args) {
        args.set("name", NAME);
    }

}
