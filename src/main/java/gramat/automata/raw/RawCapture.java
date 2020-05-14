package gramat.automata.raw;

import gramat.automata.actions.Action;
import gramat.automata.actions.PositionBegin;
import gramat.automata.actions.CommitCapture;
import gramat.automata.actions.PositionRollback;

import gramat.compiling.ValueParser;

public class RawCapture extends RawTransaction {

    private final ValueParser parser;

    public RawCapture(RawAutomaton content, ValueParser parser) {
        super(content);
        this.parser = parser;
    }

    @Override
    public RawAutomaton collapse() {
        return new RawCapture(content.collapse(), parser);
    }

    @Override
    public Action createBeginAction() {
        return new PositionBegin();
    }

    @Override
    public Action createCommitAction(Action beginAction) {
        return new CommitCapture(beginAction, parser);
    }

    @Override
    public Action createRollbackAction(Action beginAction) {
        return new PositionRollback(beginAction);
    }

}
