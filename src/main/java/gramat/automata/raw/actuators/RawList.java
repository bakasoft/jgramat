package gramat.automata.raw.actuators;

import gramat.automata.raw.RawAutomaton;
import gramat.epsilon.Builder;
import gramat.epsilon.State;
import gramat.eval.list.ListCancel;
import gramat.eval.list.ListSave;
import gramat.eval.list.ListStart;

import java.util.List;

public class RawList extends RawAutomaton {

    private final Object typeHint;
    private final RawAutomaton content;

    public RawList(RawAutomaton content, Object typeHint) {
        this.content = content;
        this.typeHint = typeHint;
    }

    @Override
    public List<RawAutomaton> getChildren() {
        return List.of(content);
    }

    @Override
    public RawAutomaton collapse() {
        return new RawList(content.collapse(), typeHint);
    }

    @Override
    public State build(Builder builder, State initial) {
        var machine = builder.machine(content, initial);
        var start = new ListStart();
        var save = new ListSave();
        var cancel = new ListCancel();
        builder.assembler.actionHook(machine, TRX.setupActions(start, save, cancel));
        return machine.accepted;
    }
}
