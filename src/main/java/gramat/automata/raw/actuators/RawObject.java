package gramat.automata.raw.actuators;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NSegment;
import gramat.automata.raw.RawAutomaton;
import gramat.epsilon.Builder;
import gramat.epsilon.State;
import gramat.eval.object.ObjectCancel;
import gramat.eval.object.ObjectSave;
import gramat.eval.object.ObjectStart;

import java.util.List;

public class RawObject extends RawAutomaton {

    private final Object typeHint;
    private final RawAutomaton content;

    public RawObject(RawAutomaton content, Object typeHint) {
        this.content = content;
        this.typeHint = typeHint;
    }

    @Override
    public List<RawAutomaton> getChildren() {
        return List.of(content);
    }

    @Override
    public RawAutomaton collapse() {
        return new RawObject(content.collapse(), typeHint);
    }

    @Override
    public NSegment build(NContext context) {
        var machine = context.machine(content);
        var start = new ObjectStart();
        var save = new ObjectSave(start);
        var cancel = new ObjectCancel(start);
//        context.actionHook(machine, TRX.setupActions(start, save, cancel));
        return machine;
    }

    @Override
    public State build(Builder builder, State initial) {
        var machine = builder.machine(content, initial);
        var start = new ObjectStart();
        var save = new ObjectSave(start);
        var cancel = new ObjectCancel(start);
        builder.assembler.actionHook(machine, TRX.setupActions(start, save, cancel));
        return machine.accepted;
    }
}
