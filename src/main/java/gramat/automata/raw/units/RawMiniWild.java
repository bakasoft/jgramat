package gramat.automata.raw.units;

import gramat.automata.ndfa.*;
import gramat.automata.raw.CollapseContext;
import gramat.automata.raw.RawAutomaton;

import java.util.HashSet;
import java.util.LinkedList;

public class RawMiniWild extends RawAutomaton {

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public void build(NContext context, NStateSet initial, NStateSet accepted) {
        context.language.transitionWild(initial, initial);

        accepted.add(initial);

        context.postBuildHook(() -> resolve_mini_wild_state(context.language, initial));
    }

    private void resolve_mini_wild_state(NLanguage language, NStateSet roots) {
        // TODO
    }

}
