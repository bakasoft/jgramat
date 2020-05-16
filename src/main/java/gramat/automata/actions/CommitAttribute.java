package gramat.automata.actions;

public class CommitAttribute extends Action {
    public final Action beginAction;
    public final String name;

    public CommitAttribute(Action beginAction, String name) {
        this.beginAction = beginAction;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Commit-Attribute:" + name;
    }
}
