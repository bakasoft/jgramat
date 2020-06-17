package gramat.automata.raw.actuators;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NSegment;
import gramat.automata.raw.RawAutomaton;
import gramat.eval.dynamicAttribute.*;

import java.util.List;

public class RawDynAttribute extends RawAutomaton {

    private final RawAutomaton name;
    private final RawAutomaton value;

    public RawDynAttribute(RawAutomaton name, RawAutomaton value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public List<RawAutomaton> getChildren() {
        return List.of(name, value);
    }

    @Override
    public RawAutomaton collapse() {
        return new RawDynAttribute(name.collapse(), value.collapse());
    }

    @Override
    public NSegment build(NContext context) {
        var nMachine = context.machine(name);
        var vMachine = context.machine(value);

        context.language.transition(nMachine.accepted, vMachine.initial, null);

        var nStart = new DynamicAttributeNameStart();
        var nSave = new DynamicAttributeNameSave(nStart);
        var nCancel = new DynamicAttributeNameCancel(nStart);
        context.actionHook(nMachine, TRX.setupActions(nStart, nSave, nCancel));

        var vStart = new DynamicAttributeValueStart();
        var vSave = new DynamicAttributeValueSave(vStart, nSave);
        var vCancel = new DynamicAttributeValueCancel(vStart);
        context.actionHook(vMachine, TRX.setupActions(vStart, vSave, vCancel));

        return context.segment(nMachine.initial, vMachine.accepted);
    }
}
