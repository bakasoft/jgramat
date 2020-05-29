package gramat.automata.raw;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NStateSet;

import java.util.List;

public class RawOptionalAutomaton extends RawAutomaton {

    private final RawAutomaton content;

    public RawOptionalAutomaton(RawAutomaton content) {
        this.content = content;
    }

    @Override
    public List<RawAutomaton> getChildren() {
        return List.of(content);
    }

    @Override
    public RawAutomaton collapse() {
        return new RawOptionalAutomaton(content.collapse());
    }

    @Override
    public void build(NContext context, NStateSet initial, NStateSet accepted) {
        // => ini => acc : c
        content.build(context, initial, accepted);

        accepted.add(initial);
    }
}
