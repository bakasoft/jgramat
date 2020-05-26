package gramat.automata.raw.actuators;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NStateSet;
import gramat.automata.raw.RawAutomaton;
import gramat.eval.join.JoinCancel;
import gramat.eval.join.JoinSave;
import gramat.eval.join.JoinStart;

public class RawJoin extends RawAutomaton {

    private final RawAutomaton content;

    public RawJoin(RawAutomaton content) {
        this.content = content;
    }

    @Override
    public RawAutomaton collapse() {
        return new RawJoin(content.collapse());
    }

    @Override
    public void build(NContext context, NStateSet initial, NStateSet accepted) {
        var machine = context.machine(content, initial, accepted);
        var start = new JoinStart();
        var save = new JoinSave();
        var cancel = new JoinCancel();
        context.postBuildHook(() -> TRX.setupActions(machine, start, save, cancel));
    }
}
