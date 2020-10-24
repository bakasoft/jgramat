package gramat.actions.transactions;

import gramat.data.MapData;
import gramat.eval.Context;
import gramat.eval.transactions.DefaultTransaction;

public class ObjectTransaction extends DefaultTransaction {

    public ObjectTransaction(int id) {
        super(id);
    }

    @Override
    public void begin(Context context) {
        context.pushContainer();
    }

    @Override
    public Runnable prepareCommit(Context context) {
        return () -> {
            var attributes = context.popAttributes();

            context.addValue(new MapData(null, attributes)); // TODO typehint
        };
    }

    @Override
    public String getName() {
        return "object";
    }

}
