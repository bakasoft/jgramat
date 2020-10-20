package gramat.actions.transactions;

import gramat.eval.Context;
import gramat.eval.RejectedException;
import gramat.eval.transactions.DefaultTransaction;

public class AttributeTransaction extends DefaultTransaction {

    private final String defaultName;

    public AttributeTransaction(int id, String defaultName) {
        super(id);
        this.defaultName = defaultName;
    }


    @Override
    public void begin(Context context) {
        context.pushContainer();
    }

    @Override
    public Runnable prepareCommit(Context context) {
        return () -> {
            var name = context.getName();
            var value = context.popValue();

            if (name == null) {
                if (defaultName == null) {
                    throw new RejectedException("missing name");
                }
                else {
                    name = defaultName;
                }
            }
            else if (defaultName != null) {
                throw new RejectedException("conflicted name");
            }

            context.setAttribute(name, value);
        };
    }
}
