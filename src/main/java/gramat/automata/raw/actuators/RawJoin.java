package gramat.automata.raw.actuators;

import gramat.automata.raw.RawAutomaton;
import gramat.epsilon.Builder;
import gramat.epsilon.State;
import gramat.eval.join.JoinCancel;
import gramat.eval.join.JoinSave;
import gramat.eval.join.JoinStart;

import java.util.List;

public class RawJoin extends RawAutomaton {

    private final RawAutomaton content;

    public RawJoin(RawAutomaton content) {
        this.content = content;
    }

    @Override
    public List<RawAutomaton> getChildren() {
        return List.of(content);
    }

    @Override
    public RawAutomaton collapse() {
        return new RawJoin(content.collapse());
    }

    @Override
    public State build(Builder builder, State initial) {
        var machine = builder.machine(content, initial);
        var start = new JoinStart();
        var save = new JoinSave();
        var cancel = new JoinCancel();
        builder.assembler.actionHook(machine, TRX.setupActions(start, save, cancel));
        return machine.accepted;
    }
}
