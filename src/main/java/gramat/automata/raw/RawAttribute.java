package gramat.automata.raw;

import gramat.automata.actions.Action;
import gramat.automata.actions.CommitAttribute;
import gramat.automata.actions.PositionBegin;
import gramat.automata.actions.PositionRollback;

public class RawAttribute extends RawTransaction {

    private final String name;

    public RawAttribute(RawAutomaton content, String name) {
        super(content);
        this.name = name;
    }

    @Override
    public Action createBeginAction() {
        return new PositionBegin();
    }

    @Override
    public Action createCommitAction(Action beginAction) {
        return new CommitAttribute(beginAction, name);
    }

    @Override
    public Action createRollbackAction(Action beginAction) {
        return new PositionRollback(beginAction);
    }

    @Override
    public RawAutomaton collapse() {
        return new RawAttribute(content.collapse(), name);
    }
}
