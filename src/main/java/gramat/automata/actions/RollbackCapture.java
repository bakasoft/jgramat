package gramat.automata.actions;

public class RollbackCapture extends Action {
    public final Action beginAction;

    public RollbackCapture(Action beginAction) {
        this.beginAction = beginAction;
    }
}
