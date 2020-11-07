package gramat.eval.transactions;

import gramat.util.NameMap;

import java.util.Map;

public abstract class TransactionAdapter implements Transaction {

    protected abstract void fillArgs(NameMap<Object> args);

    protected final int id;

    protected TransactionAdapter(int id) {
        this.id = id;
    }

    @Override
    public final int getID() {
        return id;
    }

    @Override
    public Map<String, Object> getArgs() {
        var args = new NameMap<>();
        fillArgs(args);
        return args;
    }
}
