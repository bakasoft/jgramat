package gramat.automata.raw;

import gramat.automata.actions.*;

public class RawObject extends RawTransaction {

    private final Object typeHint;

    public RawObject(RawAutomaton content, Object typeHint) {
        super(content);
        this.typeHint = typeHint;
    }

    @Override
    public Action createBeginAction() {
        return new ObjectBegin();
    }

    @Override
    public Action createCommitAction(Action beginAction) {
        return new ObjectCommit();
    }

    @Override
    public Action createRollbackAction(Action beginAction) {
        return new ObjectRollback();
    }

    @Override
    public RawAutomaton collapse() {
        return new RawObject(content.collapse(), typeHint);
    }
}
