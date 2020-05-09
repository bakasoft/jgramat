package gramat.automata.raw;

import gramat.automata.nondet.NAutomaton;
import gramat.automata.nondet.NLanguage;
import gramat.automata.nondet.NState;

import static gramat.util.ListTool.iterate;

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
    public NAutomaton build(NLanguage lang) {
        var array = value.toCharArray();
        NState start = lang.state();
        NState last = start;
        for (char c : array) {
            last = last.linkChar(c);
        }
        return lang.automaton(start, start, last);
    }

    @Override
    public String getStringValue() {
        return value;
    }
}
