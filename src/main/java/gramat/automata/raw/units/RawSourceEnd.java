package gramat.automata.raw.units;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NStateSet;
import gramat.automata.raw.CollapseContext;
import gramat.automata.raw.RawAutomaton;
import gramat.util.parsing.Source;

import java.util.List;

public class RawSourceEnd extends RawAutomaton {

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
        accepted.notEmpty(context);

        var symbol = context.language.symbols.getChar(Source.EOF);
        context.language.transition(initial, accepted, symbol);
    }
}
