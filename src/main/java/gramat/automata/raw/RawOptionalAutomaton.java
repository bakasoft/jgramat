package gramat.automata.raw;

import gramat.automata.ndfa.Language;
import gramat.automata.ndfa.NAutomaton;
import gramat.automata.ndfa.NState;

import java.util.HashSet;

public class RawOptionalAutomaton extends RawAutomaton {

    private final RawAutomaton content;

    public RawOptionalAutomaton(RawAutomaton content) {
        this.content = content;
    }

    @Override
    public RawAutomaton collapse() {
        return new RawOptionalAutomaton(content.collapse());
    }

    @Override
    public NAutomaton build(Language lang) {
        var am = content.build(lang);

        am.accepted.addAll(am.initial);

        return am;
    }
}
