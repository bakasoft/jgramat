package gramat.automata.actions;

import gramat.compiling.ValueParser;

public class CommitCapture extends Action {

    public final Action beginAction;
    public final ValueParser parser;

    public CommitCapture(Action beginAction, ValueParser parser) {
        this.beginAction = beginAction;
        this.parser = parser;
    }

    @Override
    public String toString() {
        return "Commit-Capture";
    }
}
