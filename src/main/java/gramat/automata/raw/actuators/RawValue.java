package gramat.automata.raw.actuators;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NSegment;
import gramat.automata.raw.RawAutomaton;
import gramat.compiling.ValueParser;
import gramat.epsilon.Builder;
import gramat.epsilon.State;
import gramat.eval.value.ValueCancel;
import gramat.eval.value.ValueSave;
import gramat.eval.value.ValueStart;

import java.util.List;

public class RawValue extends RawAutomaton {

    private final RawAutomaton content;
    private final ValueParser parser;

    public RawValue(RawAutomaton content, ValueParser parser) {
        this.content = content;
        this.parser = parser;
    }

    @Override
    public List<RawAutomaton> getChildren() {
        return List.of(content);
    }

    @Override
    public RawAutomaton collapse() {
        return new RawValue(content.collapse(), parser);
    }

    @Override
    public NSegment build(NContext context) {
        var machine = context.machine(content);
        var start = new ValueStart(parser);
        var save = new ValueSave(start, parser);
        var cancel = new ValueCancel(start, parser);
//        context.actionHook(machine, TRX.setupActions(start, save, cancel));
        return machine;
    }

    @Override
    public State build(Builder builder, State initial) {
        var machine = builder.machine(content, initial);
        var start = new ValueStart(parser);
        var save = new ValueSave(start, parser);
        var cancel = new ValueCancel(start, parser);
        builder.assembler.actionHook(machine, TRX.setupActions(start, save, cancel));
        return machine.accepted;
    }

}
