package gramat.automata.actions;

public class CommitDynAttribute extends Action {
    public final Action beginAction;
    public CommitDynAttribute(Action beginAction) {
        this.beginAction = beginAction;
    }
}
