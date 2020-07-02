package gramat.automata.raw.units;

import gramat.automata.raw.RawAutomaton;
import gramat.epsilon.Builder;
import gramat.epsilon.State;

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
    public State build(Builder builder, State initial) {
        builder.newWildTransition(initial, initial);

        builder.assembler.linkHook(initial, RawMiniWild::resolve_mini_wild_state);

        return initial;
    }

    private static void resolve_mini_wild_state(State root) {
        // TODO
    }

}
