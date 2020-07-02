package gramat.automata.raw;

import gramat.epsilon.Builder;
import gramat.epsilon.State;

import java.util.List;

import static gramat.util.ListTool.removeNulls;


public class RawRepetitionAutomaton extends RawAutomaton {

    private final RawAutomaton content;
    private final RawAutomaton separator;
    private final Integer minimum;
    private final Integer maximum;

    public RawRepetitionAutomaton(RawAutomaton content, RawAutomaton separator, Integer minimum, Integer maximum) {
        this.content = content;
        this.separator = separator;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public List<RawAutomaton> getChildren() {
        return removeNulls(content, separator);
    }

    @Override
    public RawAutomaton collapse() {
        return new RawRepetitionAutomaton(
                content.collapse(),
                separator != null ? separator.collapse() : null,
                minimum,
                maximum
        );
    }

    @Override
    public State build(Builder builder, State initial) {
        var min = (minimum != null ? minimum : 0);
        var max = (maximum != null ? maximum : 0);

        if (min < 0 || max < 0) {
            throw new RuntimeException("invalid values");
        }

        if (min == 0 && max == 0) {
            if (separator == null) {
                return zero_or_more_no_separator(builder, initial);
            }
            else {
                return zero_or_more_with_separator(builder, initial);
            }
        }
        else if (min == 1 && max == 0) {
            if (separator == null) {
                return one_or_more_no_separator(builder, initial);
            }
            else {
                return one_or_more_with_separator(builder, initial);
            }
        }
        else if (min > 1 && max == 0) {
            return at_least_n_times(builder, initial, min);
        }
        else if (min == max) {
            return exact_n_times(builder, initial, min);
        }
        else {
            return at_least_but_no_more_times(builder, initial, min, max);
        }
    }

    private State zero_or_more_no_separator(Builder builder, State initial) {
        // one time
        var accepted = content.build(builder, initial);

        // zero times
        builder.newNullTransition(initial, accepted);

        // more times
        builder.newNullTransition(accepted, initial);

        return accepted;
    }

    private State zero_or_more_with_separator(Builder builder, State initial) {
        // one time
        var accepted = content.build(builder, initial);

        // zero times
        builder.newNullTransition(initial, accepted);

        // more times
        var aux = separator.build(builder, accepted);
        builder.newNullTransition(aux, initial);

        return accepted;
    }

    private State one_or_more_no_separator(Builder builder, State initial) {
        // one time
        var accepted = content.build(builder, initial);

        // more times
        builder.newNullTransition(accepted, initial);

        return accepted;
    }

    private State one_or_more_with_separator(Builder builder, State initial) {
        // one time
        var accepted = content.build(builder, initial);

        // more times
        var aux = separator.build(builder, accepted);
        builder.newNullTransition(aux, initial);

        return accepted;
    }

    private State at_least_n_times(Builder builder, State initial, int count) {
        throw new UnsupportedOperationException();  // TODO
    }

    private State exact_n_times(Builder builder, State initial, int count) {
        var last = initial;

        for (var i = 0; i < count; i++) {
            if (i > 0 && separator != null) {
                last = separator.build(builder, last);
            }

            last = content.build(builder, last);
        }

        return last;
    }

    private State at_least_but_no_more_times(Builder builder, State initial, int min, int max) {
        throw new UnsupportedOperationException();  // TODO
    }
}
