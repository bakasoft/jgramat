package gramat.automata.actions;

public class PositionRollback extends Action {
    public final Action beginAction;

    public PositionRollback(Action beginAction) {
        this.beginAction = beginAction;
    }

    @Override
    public String toString() {
        return "Position-Rollback";
    }
}
