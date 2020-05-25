package gramat.automata.raw.actuators;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NStateSet;
import gramat.automata.raw.RawAutomaton;
import gramat.eval.dynamicAttribute.*;

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
    public void build(NContext context, NStateSet initial, NStateSet accepted) {
        var nMachine = context.machine(name, initial);
        var vMachine = context.machine(value, NStateSet.of(nMachine.accepted));

        accepted.add(vMachine.accepted);

        var nStart = new DynamicAttributeNameStart();
        var nSave = new DynamicAttributeNameSave(nStart);
        var nCancel = new DynamicAttributeNameCancel(nStart);
        context.postBuildHook(() -> TRX.setupActions(nMachine, nStart, nSave, nCancel));

        var vStart = new DynamicAttributeValueStart();
        var vSave = new DynamicAttributeValueSave(nStart);
        var vCancel = new DynamicAttributeValueCancel(nStart);
        context.postBuildHook(() -> TRX.setupActions(vMachine, vStart, vSave, vCancel));
    }
}
