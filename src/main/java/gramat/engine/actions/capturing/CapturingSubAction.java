package gramat.engine.actions.capturing;

abstract public class CapturingSubAction<T extends CapturingAction> extends CapturingAction {

    public final T origin;

    public CapturingSubAction(T origin) {
        this.origin = origin;
    }

}
