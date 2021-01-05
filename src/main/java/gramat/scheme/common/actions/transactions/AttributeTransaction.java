package gramat.scheme.common.actions.transactions;

import gramat.eval.EvalContext;
import gramat.eval.RejectedException;
import gramat.eval.transactions.TransactionAdapter;
import gramat.util.NameMap;

public class AttributeTransaction extends TransactionAdapter {

    public static final String NAME = "attribute";

    private final String defaultName;

    public AttributeTransaction(int id, String defaultName) {
        super(id);
        this.defaultName = defaultName;
    }

    public String getDefaultName() {
        return defaultName;
    }

    @Override
    public void begin(EvalContext context) {
        context.pushContainer();
    }

    @Override
    public Runnable prepareCommit(EvalContext context) {
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

    @Override
    protected void fillArgs(NameMap<Object> args) {
        args.set("name", NAME);
        args.set("defaultName", defaultName);
    }
}
