package gramat.actions.transactions;

import gramat.eval.Context;
import gramat.eval.transactions.DefaultTransaction;

public class ArrayTransaction extends DefaultTransaction {

    private final Object typeHint;

    public ArrayTransaction(int id, Object typeHint) {
        super(id);
        this.typeHint = typeHint;
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

}
