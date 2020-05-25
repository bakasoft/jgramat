package gramat.automata.raw;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NStateSet;

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
    public void build(NContext context, NStateSet q1, NStateSet accepted) {
        // => q1 => q2 : c
        var q2 = new NStateSet();

        content.build(context, q1, q2);

        accepted.add(q1, q2);
    }
}
