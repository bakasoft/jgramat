package gramat.scheme.common.actions.transactions;

import gramat.eval.EvalContext;
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
    public void begin(EvalContext context) {
        context.pushContainer();
    }

    @Override
    public Runnable prepareCommit(EvalContext context) {
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
