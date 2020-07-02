package gramat.automata.raw;

import gramat.epsilon.Builder;
import gramat.epsilon.State;

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
    public State build(Builder builder, State initial) {
        var accepted = content.build(builder, initial);

        builder.newNullTransition(initial, accepted);

        return accepted;
    }

}
