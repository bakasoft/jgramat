package gramat.automata.raw.actuators;

import gramat.automata.raw.RawAutomaton;
import gramat.epsilon.Builder;
import gramat.epsilon.State;
import gramat.eval.staticAttribute.StaticAttributeCancel;
import gramat.eval.staticAttribute.StaticAttributeSave;
import gramat.eval.staticAttribute.StaticAttributeStart;

import java.util.List;

public class RawAttribute extends RawAutomaton {

    private final RawAutomaton content;
    private final String name;

    public RawAttribute(String name, RawAutomaton content) {
        this.content = content;
        this.name = name;
    }

    @Override
    public List<RawAutomaton> getChildren() {
        return List.of(content);
    }

    @Override
    public RawAutomaton collapse() {
        return new RawAttribute(name, content.collapse());
    }

    @Override
    public State build(Builder builder, State initial) {
        var machine = builder.machine(content, initial);
        var start = new StaticAttributeStart();
        var save = new StaticAttributeSave(start, name);
        var cancel = new StaticAttributeCancel(start);
        builder.assembler.actionHook(machine, TRX.setupActions(start, save, cancel));
        return machine.accepted;
    }
}
