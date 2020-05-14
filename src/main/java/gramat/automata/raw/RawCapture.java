package gramat.automata.raw;

import gramat.automata.ndfa.Language;
import gramat.automata.ndfa.NAutomaton;

public class RawCapture extends RawAutomaton {

    private final RawAutomaton content;

    public RawCapture(RawAutomaton content) {
        this.content = content;
    }

    @Override
    public RawAutomaton collapse() {
        return new RawCapture(content.collapse());
    }

    @Override
    public NAutomaton build(Language lang) {
        var initial = lang.state();
        var accepted = lang.state();
        var rejected = lang.state();

        var am = content.build(lang);

        lang.transition(initial, am.initial, null, "BEGIN");
        lang.transition(am.accepted, accepted, null, "COMMIT");
        lang.transition(am.getRejected(), rejected, null, "ROLLBACK");

        return lang.automaton(initial, accepted);
    }

}
