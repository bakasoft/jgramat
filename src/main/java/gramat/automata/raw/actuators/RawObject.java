package gramat.automata.raw.actuators;

import gramat.automata.ndfa.NContext;
import gramat.automata.raw.RawAutomaton;
import gramat.eval.object.ObjectCancel;
import gramat.eval.object.ObjectSave;
import gramat.eval.object.ObjectStart;

public class RawObject extends RawAutomaton {

    private final Object typeHint;
    private final RawAutomaton content;

    public RawObject(RawAutomaton content, Object typeHint) {
        this.content = content;
        this.typeHint = typeHint;
    }

    @Override
    public RawAutomaton collapse() {
        return new RawObject(content.collapse(), typeHint);
    }

    @Override
    public void build(NContext context) {
        content.build(context);

        var machine = context.machine();
        var start = new ObjectStart();
        var save = new ObjectSave();
        var cancel = new ObjectCancel();
        context.postBuildHook(() -> TRX.setupActions(machine, start, save, cancel));
    }
}
