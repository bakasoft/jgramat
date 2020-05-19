package gramat.automata.raw;

import gramat.automata.ndfa.NContext;

public class RawSourceBegin extends RawAutomaton {
    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public void build(NContext context) {
        context.initialAccepted();
    }
}
