package gramat.automata.raw.actuators;

import gramat.automata.ndfa.NContext;
import gramat.automata.raw.RawAutomaton;
import gramat.eval.dynamicAttribute.DynamicAttributeCancel;
import gramat.eval.dynamicAttribute.DynamicAttributeSave;
import gramat.eval.dynamicAttribute.DynamicAttributeStart;

public class RawDynAttribute extends RawAutomaton {

    private final RawAutomaton name;
    private final RawAutomaton value;

    public RawDynAttribute(RawAutomaton name, RawAutomaton value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public RawAutomaton collapse() {
        return new RawDynAttribute(name.collapse(), value.collapse());
    }

    @Override
    public void build(NContext context) {
        var nMachine = context.subMachine(name);
        var vMachine = context.subMachine(value);

        context.transitionNull(nMachine.accepted, vMachine.initial);

        context.initial(nMachine.initial);
        context.accepted(vMachine.accepted);

        var start = new DynamicAttributeStart();
        var save = new DynamicAttributeSave(start);
        var cancel = new DynamicAttributeCancel(start);
        var machine = context.machine();
        context.postBuildHook(() -> TRX.setupActions(machine, start, save, cancel));
    }
}
