package gramat.automata.raw;

import gramat.automata.ndfa.NAutomaton;
import gramat.automata.ndfa.Language;


public class RawRepetitionAutomaton extends RawAutomaton {

    private final RawAutomaton content;
    private final RawAutomaton separator;
    private final Integer minimum;
    private final Integer maximum;

    public RawRepetitionAutomaton(RawAutomaton content, RawAutomaton separator, Integer minimum, Integer maximum) {
        this.content = content;
        this.separator = separator;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public RawAutomaton collapse() {
        return new RawRepetitionAutomaton(
                content.collapse(),
                separator != null ? separator.collapse() : null,
                minimum,
                maximum
        );
    }

    @Override
    public NAutomaton build(Language lang) {
        if (separator == null && minimum == null && maximum == null) {
            var state = lang.state();

            var am = content.build(lang);

            lang.transition(state, am.initial, null);

            lang.transition(am.accepts, state, null);

            return lang.automaton(state, state);
        }
        else {
            throw new RuntimeException("not implemented");
        }
    }

}
