package gramat.automata.raw.actuators;

import gramat.automata.ndfa.Language;
import gramat.automata.ndfa.NAutomaton;
import gramat.automata.raw.RawAutomaton;

public class RawDynAttribute extends RawAutomaton {

    private final RawAutomaton name;
    private final RawAutomaton content;

    public RawDynAttribute(RawAutomaton name, RawAutomaton content) {
        this.name = name;
        this.content = content;
    }

    @Override
    public RawAutomaton collapse() {
        return new RawDynAttribute(name.collapse(), content.collapse());
    }

    @Override
    public NAutomaton build(Language lang) {
        return lang.automaton((initialSet, acceptedSet) -> {
            var amName = name.build(lang);
            var amContent = content.build(lang);

            lang.transition(amName.accepted, amContent.initial, null);

            initialSet.add(amName.initial);
            acceptedSet.add(amContent.accepted);
        });
    }
}
