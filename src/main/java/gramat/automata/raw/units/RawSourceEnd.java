package gramat.automata.raw.units;

import gramat.automata.ndfa.NContext;
import gramat.automata.raw.RawAutomaton;
import gramat.util.parsing.Source;

public class RawSourceEnd extends RawAutomaton {
    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public void build(NContext context) {
        var initial = context.initial();
        var accepted = context.accepted();

        context.transitionChar(initial, accepted, Source.EOF);
    }
}
