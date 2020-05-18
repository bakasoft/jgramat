package gramat.eval;

abstract public class SubAction extends Action {

    public final Action origin;

    public SubAction(Action origin) {
        this.origin = origin;
    }

    @Override
    public final String toString() {
        return getDescription() + " (from #" + origin.id + ")";
    }

}
