package gramat.automata.raw.actuators;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NStateSet;
import gramat.automata.raw.CollapseContext;
import gramat.automata.raw.RawAutomaton;
import gramat.compiling.ValueParser;
import gramat.eval.value.ValueCancel;
import gramat.eval.value.ValueSave;
import gramat.eval.value.ValueStart;

public class RawValue extends RawAutomaton {

    private final RawAutomaton content;
    private final ValueParser parser;

    public RawValue(RawAutomaton content, ValueParser parser) {
        this.content = content;
        this.parser = parser;
    }

    @Override
    public RawAutomaton collapse() {
        return new RawValue(content.collapse(), parser);
    }

    @Override
    public void build(NContext context, NStateSet initial, NStateSet accepted) {
        var machine = context.machine(content, initial, accepted);
        var start = new ValueStart(parser);
        var save = new ValueSave(start, parser);
        var cancel = new ValueCancel(start, parser);
        context.postBuildHook(() -> TRX.setupActions(machine, start, save, cancel));
    }

}
