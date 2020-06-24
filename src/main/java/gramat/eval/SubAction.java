package gramat.eval;

abstract public class SubAction<T extends TRXAction> extends TRXAction {

    public final T origin;

    public SubAction(T origin) {
        this.origin = origin;
    }

    @Override
    public final String toString() {
        return getDescription() + " (from #" + origin.id + ")";
    }

}
