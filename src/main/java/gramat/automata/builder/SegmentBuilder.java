package gramat.automata.builder;

import gramat.automata.State;

import java.util.HashMap;
import java.util.HashSet;

public class SegmentBuilder {  // TODO remove this class methods should return just the end state

    public final AutomatonBuilder builder;
    public final StateBuilder begin;
    public final StateBuilder end;

    public SegmentBuilder(AutomatonBuilder builder, StateBuilder begin, StateBuilder end) {
        this.builder = builder;
        this.begin = begin;
        this.end = end;
    }

    public State compile() {
        var states = new HashMap<StateBuilder, State>();

        for (var sb : builder.getStates()) {
            var st = new State(sb.accepted != null ? sb.accepted : false);

            states.put(sb, st);
        }

        for (var tb : builder.getTransitions()) {
            var source = states.get(tb.source);
            var target = states.get(tb.target);

            if (source == null || target == null) {
                throw new RuntimeException();
            }

            if (tb.condition instanceof ConditionChar) {
                var cc = (ConditionChar) tb.condition;
                source.addTransition(target, cc.value);
            }
            else if (tb.condition instanceof ConditionRange) {
                var cr = (ConditionRange) tb.condition;
                source.addTransition(target, cr.begin, cr.end);
            }
            else {
                throw new RuntimeException();
            }
        }

        var result = states.get(begin);

        if (result == null) {
            throw new RuntimeException();
        }

        return result;
    }

}
