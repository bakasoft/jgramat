package gramat.automata.raw.units;

import gramat.automata.ndfa.*;
import gramat.automata.raw.CollapseContext;
import gramat.automata.raw.RawAutomaton;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class RawMiniWild extends RawAutomaton {

    @Override
    public List<RawAutomaton> getChildren() {
        return List.of();
    }

    @Override
    public RawAutomaton collapse() {
        return this;
    }

    @Override
    public void build(NContext context, NStateSet initial, NStateSet accepted) {
        var wild = context.language.symbols.getWild();
        context.language.transition(initial, initial, wild);

        accepted.add(initial);

        context.postBuildHook(() -> resolve_mini_wild_state(context.language, initial));
    }

    private void resolve_mini_wild_state(NLanguage language, NStateSet roots) {
        // TODO
    }

}
