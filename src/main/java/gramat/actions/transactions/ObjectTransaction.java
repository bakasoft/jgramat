package gramat.actions.transactions;

import gramat.data.MapData;
import gramat.eval.Context;
import gramat.eval.transactions.TransactionAdapter;
import gramat.util.NameMap;

public class ObjectTransaction extends TransactionAdapter {

    public static final String NAME = "object";
    private final String typeHint;

    public ObjectTransaction(int id, String typeHint) {
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
            var attributes = context.popAttributes();

            context.addValue(new MapData(null, attributes)); // TODO typehint
        };
    }

    @Override
    protected void fillArgs(NameMap<Object> args) {
        args.set("name", NAME);
        args.set("typeHint", typeHint);
    }

}
