package gramat.automata.raw.actuators;

import gramat.eval.*;

import gramat.automata.raw.RawAutomaton;
import gramat.compiling.ValueParser;
import gramat.eval.value.ValueCancel;
import gramat.eval.value.ValueSave;
import gramat.eval.value.ValueStart;

public class RawCapture extends RawTransaction {

    private final ValueParser parser;

    public RawCapture(RawAutomaton content, ValueParser parser) {
        super(content);
        this.parser = parser;
    }

    @Override
    public RawAutomaton collapse() {
        return new RawCapture(content.collapse(), parser);
    }

    @Override
    public Action createBeginAction() {
        return new ValueStart(parser);
    }

    @Override
    public Action createCommitAction(Action beginAction) {
        return new ValueSave(beginAction, parser);
    }

    @Override
    public ValueCancel createRollbackAction(Action beginAction) {
        return new ValueCancel(beginAction, parser);
    }

}
