package gramat.automata.raw.actuators;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NSegment;
import gramat.automata.ndfa.NStateSet;
import gramat.automata.raw.CollapseContext;
import gramat.automata.raw.RawAutomaton;
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
    public NSegment build(NContext context) {
        var machine = context.machine(content);
        var start = new StaticAttributeStart();
        var save = new StaticAttributeSave(start, name);
        var cancel = new StaticAttributeCancel(start);
        context.machineHook(machine, TRX.setupActions(start, save, cancel));
        return machine;
    }
}
