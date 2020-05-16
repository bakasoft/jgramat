package gramat.automata.raw;

import gramat.automata.ndfa.NAutomaton;
import gramat.automata.ndfa.Language;

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
        var initial = lang.state();
        var last = initial;
        for (var c : value.toCharArray()) {
            var next = lang.state();
            lang.transition(last, next, (int)c);
            last = next;
        }
        return lang.automaton(initial, last);
    }

    @Override
    public String getStringValue() {
        return value;
    }
}
