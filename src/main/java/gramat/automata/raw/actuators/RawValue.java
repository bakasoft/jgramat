package gramat.automata.raw.actuators;

import gramat.automata.ndfa.Language;
import gramat.automata.ndfa.NAutomaton;
import gramat.eval.*;

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
    public NAutomaton build(Language lang) {
        var am = content.build(lang);
        var start = new ValueStart(parser);
        var save = new ValueSave(start, parser);
        var cancel = new ValueCancel(start, parser);
        lang.postBuild(() -> TRX.setupActions(am, start, save, cancel));
        return am;
    }

}
