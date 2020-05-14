package gramat.automata.actions;

public class RollbackCapture extends Action {
    public final BeginCapture beginAction;

    public RollbackCapture(BeginCapture beginAction) {
        this.beginAction = beginAction;
    }
}
