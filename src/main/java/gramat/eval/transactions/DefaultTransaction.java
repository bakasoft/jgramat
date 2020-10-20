package gramat.eval.transactions;

public abstract class DefaultTransaction implements Transaction {

    protected final int id;

    protected DefaultTransaction(int id) {
        this.id = id;
    }

    @Override
    public final int getID() {
        return id;
    }

}
