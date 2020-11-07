package gramat.actions.transactions;

import gramat.eval.Context;
import gramat.eval.transactions.TransactionAdapter;
import gramat.util.NameMap;

public class ArrayTransaction extends TransactionAdapter {

    public static final String NAME = "array";

    private final String typeHint;

    public ArrayTransaction(int id, String typeHint) {
        super(id);
        this.typeHint = typeHint;
    }

    public String getTypeHint() {
        return typeHint;
    }

    @Override
    public void begin(Context context) {
        context.pushContainer();
    }

    @Override
    public Runnable prepareCommit(Context context) {
        return () -> {
            var array = context.popArray();

            // TODO use typeHint

            context.addValue(array);
        };
    }

    @Override
    protected void fillArgs(NameMap<Object> args) {
        args.set("name", NAME);
        args.set("typeHint", typeHint);
    }
}
