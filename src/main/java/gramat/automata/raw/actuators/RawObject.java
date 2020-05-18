package gramat.automata.raw.actuators;

import gramat.automata.raw.RawAutomaton;
import gramat.eval.Action;
import gramat.eval.object.ObjectCancel;
import gramat.eval.object.ObjectSave;
import gramat.eval.object.ObjectStart;

public class RawObject extends RawTransaction {

    private final Object typeHint;

    public RawObject(RawAutomaton content, Object typeHint) {
        super(content);
        this.typeHint = typeHint;
    }

    @Override
    public Action createBeginAction() {
        return new ObjectStart();
    }

    @Override
    public Action createCommitAction(Action beginAction) {
        return new ObjectSave();
    }

    @Override
    public Action createRollbackAction(Action beginAction) {
        return new ObjectCancel();
    }

    @Override
    public RawAutomaton collapse() {
        return new RawObject(content.collapse(), typeHint);
    }
}
