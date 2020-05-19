package gramat.automata.raw;

import gramat.automata.ndfa.NContext;

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
    public void build(NContext context) {
        content.build(context);

        // make initial states accepted as well
        context.accepted(context.getInitialStates());
    }
}
