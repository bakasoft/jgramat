package gramat.automata.actions;

import gramat.compiling.ValueParser;

public class CommitCapture extends Action {

    public final BeginCapture beginAction;
    public final ValueParser parser;

    public CommitCapture(BeginCapture beginAction, ValueParser parser) {
        this.beginAction = beginAction;
        this.parser = parser;
    }
}
