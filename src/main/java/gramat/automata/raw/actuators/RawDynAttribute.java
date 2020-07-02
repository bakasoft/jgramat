package gramat.automata.raw.actuators;

import gramat.automata.raw.RawAutomaton;
import gramat.epsilon.Builder;
import gramat.epsilon.State;
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
    public State build(Builder builder, State initial) {
        var nMachine = builder.machine(name, initial);
        var vMachine = builder.machine(value, nMachine.accepted);

        var nStart = new DynamicAttributeNameStart();
        var nSave = new DynamicAttributeNameSave(nStart);
        var nCancel = new DynamicAttributeNameCancel(nStart);
        builder.assembler.actionHook(nMachine, TRX.setupActions(nStart, nSave, nCancel));

        var vStart = new DynamicAttributeValueStart();
        var vSave = new DynamicAttributeValueSave(vStart, nSave);
        var vCancel = new DynamicAttributeValueCancel(vStart);
        builder.assembler.actionHook(vMachine, TRX.setupActions(vStart, vSave, vCancel));

        return vMachine.accepted;
    }
}
