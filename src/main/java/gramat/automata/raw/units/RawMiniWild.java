package gramat.automata.raw.units;

import gramat.automata.ndfa.*;
import gramat.automata.raw.CollapseContext;
import gramat.automata.raw.RawAutomaton;
import gramat.epsilon.Builder;
import gramat.epsilon.State;

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
    public NSegment build(NContext context) {
        var state = context.language.state();
        var wild = context.language.symbols.getWild();

        context.language.transition(state, state, wild);

//        context.linkHook(state, RawMiniWild::resolve_mini_wild_state);

        return context.segment(state, state);
    }

    @Override
    public State build(Builder builder, State initial) {
        builder.newWildTransition(initial, initial);

        builder.assembler.linkHook(initial, RawMiniWild::resolve_mini_wild_state);

        return initial;
    }

    private static void resolve_mini_wild_state(State root) {
        // TODO
    }

}
