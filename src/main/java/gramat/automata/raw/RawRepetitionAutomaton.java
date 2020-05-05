package gramat.automata.raw;

import gramat.automata.builder.AutomatonBuilder;
import gramat.automata.builder.SegmentBuilder;
import gramat.automata.builder.StateBuilder;

public class RawRepetitionAutomaton extends RawAutomaton {

    private final RawAutomaton automaton;

    public RawRepetitionAutomaton(RawAutomaton automaton) {
        this.automaton = automaton;
    }

    @Override
    public RawAutomaton collapse() {
        return new RawRepetitionAutomaton(automaton.collapse());
    }

    @Override
    public SegmentBuilder build(AutomatonBuilder builder, StateBuilder s0) {
        var forwardSegment = automaton.build(builder, s0);
        var sF = forwardSegment.end;
        var backwardSegment = automaton.build(builder, sF);

        builder.replace(backwardSegment.end, s0);

        var segment = builder.createSegment(s0, sF);
        s0.makeAccepted();
        sF.makeAccepted();
        return segment;
    }

}
