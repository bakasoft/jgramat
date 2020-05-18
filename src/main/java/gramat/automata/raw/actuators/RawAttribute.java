package gramat.automata.raw.actuators;

import gramat.automata.raw.RawAutomaton;
import gramat.eval.Action;
import gramat.eval.staticAttribute.StaticAttributeCancel;
import gramat.eval.staticAttribute.StaticAttributeSave;
import gramat.eval.staticAttribute.StaticAttributeStart;

public class RawAttribute extends RawTransaction {

    private final String name;

    public RawAttribute(RawAutomaton content, String name) {
        super(content);
        this.name = name;
    }

    @Override
    public Action createBeginAction() {
        return new StaticAttributeStart();
    }

    @Override
    public Action createCommitAction(Action beginAction) {
        return new StaticAttributeSave(beginAction, name);
    }

    @Override
    public Action createRollbackAction(Action beginAction) {
        return new StaticAttributeCancel(beginAction);
    }

    @Override
    public RawAutomaton collapse() {
        return new RawAttribute(content.collapse(), name);
    }
}
