package gramat.automata.raw.units;

import gramat.automata.ndfa.NAutomaton;
import gramat.automata.ndfa.Language;
import gramat.automata.raw.RawAutomaton;
import gramat.automata.raw.RawStringAutomaton;

public class RawLiteralAutomaton extends RawStringAutomaton {

    private final String value;

    public RawLiteralAutomaton(String value) {
        this.value = value;

        if (value.isEmpty()) {
            throw new RuntimeException();
        }
    }

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public NAutomaton build(Language lang) {
        return lang.automaton((initialSet, acceptedSet) -> {
            var last = initialSet.create();
            for (var c : value.toCharArray()) {
                var next = lang.state();
                lang.transition(last, next, (int)c);
                last = next;
            }
            acceptedSet.add(last);
        });
    }

    @Override
    public String getStringValue() {
        return value;
    }
}
