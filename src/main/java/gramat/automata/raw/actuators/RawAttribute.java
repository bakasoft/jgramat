package gramat.automata.raw.actuators;

import gramat.automata.ndfa.NContext;
import gramat.automata.raw.RawAutomaton;
import gramat.eval.staticAttribute.StaticAttributeCancel;
import gramat.eval.staticAttribute.StaticAttributeSave;
import gramat.eval.staticAttribute.StaticAttributeStart;

public class RawAttribute extends RawAutomaton {

    private final RawAutomaton content;
    private final String name;

    public RawAttribute(RawAutomaton content, String name) {
        this.content = content;
        this.name = name;
    }

    @Override
    public RawAutomaton collapse() {
        return new RawAttribute(content.collapse(), name);
    }

    @Override
    public void build(NContext context) {
        content.build(context);

        var start = new StaticAttributeStart();
        var save = new StaticAttributeSave(start, name);
        var cancel = new StaticAttributeCancel(start);
        var machine = context.machine();
        context.postBuildHook(() -> TRX.setupActions(machine, start, save, cancel));
    }
}
