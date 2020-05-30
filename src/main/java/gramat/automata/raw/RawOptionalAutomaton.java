package gramat.automata.raw;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NSegment;
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
    public NSegment build(NContext context) {
        var initial = context.language.state();
        var accepted = context.language.state();
        var segment = content.build(context);

        context.language.transition(initial, accepted, null);
        context.language.transition(initial, segment.initial, null);
        context.language.transition(segment.accepted, accepted, null);

        return context.segment(initial, accepted);
    }
}
