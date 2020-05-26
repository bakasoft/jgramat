package gramat.parsing;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NStateSet;
import gramat.automata.raw.RawAutomaton;

public class RawAutomataPromise extends RawAutomaton {

    private final Parser parser;
    private final String name;

    public RawAutomataPromise(Parser parser, String name) {
        this.parser = parser;
        this.name = name;
    }

    @Override
    public RawAutomaton collapse() {
        return parser.findExpression(name);
    }

    @Override
    public void build(NContext context, NStateSet initial, NStateSet accepted) {
        throw new RuntimeException("Promise not resolved");
    }
}
