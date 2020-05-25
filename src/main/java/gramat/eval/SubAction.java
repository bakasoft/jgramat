package gramat.eval;

abstract public class SubAction<T extends Action> extends Action {

    public final T origin;

    public SubAction(T origin) {
        this.origin = origin;
    }

    @Override
    public final String toString() {
        return getDescription() + " (from #" + origin.id + ")";
    }

}
