package gramat.automata.raw;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NSegment;
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
    public NSegment build(NContext context) {
        var min = (minimum != null ? minimum : 0);
        var max = (maximum != null ? maximum : 0);

        if (min < 0 || max < 0) {
            throw new RuntimeException("invalid values");
        }

        if (min == 0 && max == 0) {
            if (separator == null) {
                return zero_or_more_no_separator(context);
            }
            else {
                return zero_or_more_with_separator(context);
            }
        }
        else if (min == 1 && max == 0) {
            if (separator == null) {
                return one_or_more_no_separator(context);
            }
            else {
                return one_or_more_with_separator(context);
            }
        }
        else if (min > 1 && max == 0) {
            return at_least_n_times(context, min);
        }
        else if (min == max) {
            return exact_n_times(context, min);
        }
        else {
            return at_least_but_no_more_times(context, min, max);
        }
    }

    private NSegment zero_or_more_no_separator(NContext context) {
        var initial = context.language.state();
        var accepted = context.language.state();
        var segment = content.build(context);

        context.language.transition(initial, accepted, null);
        context.language.transition(initial, segment.initial, null);
        context.language.transition(segment.accepted, accepted, null);
        context.language.transition(segment.accepted, segment.initial, null);

        return context.segment(initial, accepted);
    }

    private NSegment zero_or_more_with_separator(NContext context) {
        var initial = context.language.state();
        var accepted = context.language.state();

        var con = content.build(context);
        var sep = separator.build(context);

        // zero times
        context.language.transition(initial, accepted, null);

        // one time
        var auxOne = context.language.state();
        context.language.transition(initial, con.initial, null);
        context.language.transition(con.accepted, auxOne, null);
        context.language.transition(auxOne, accepted, null);

        // more times with separator
        context.language.transition(auxOne, sep.initial, null);
        context.language.transition(sep.accepted, con.initial, null);

        return context.segment(initial, accepted);
    }

    private NSegment one_or_more_no_separator(NContext context) {
        var initial = context.language.state();
        var accepted = context.language.state();

        var required = content.build(context);

        context.language.transition(initial, required.initial, null);
        context.language.transition(required.accepted, accepted, null);

        var optional = content.build(context);

        context.language.transition(accepted, optional.initial, null);
        context.language.transition(optional.accepted, accepted, null);

        return context.segment(initial, accepted);
    }

    private NSegment one_or_more_with_separator(NContext context) {
        var initial = context.language.state();
        var accepted = context.language.state();

        var required = content.build(context);
        var separator = this.separator.build(context);
        var optional = content.build(context);

        // one time
        var aux = context.language.state();
        context.language.transition(initial, required.initial, null);
        context.language.transition(required.accepted, aux, null);
        context.language.transition(aux, accepted, null);

        // more times with separator
        context.language.transition(aux, separator.initial, null);
        context.language.transition(separator.accepted, optional.initial, null);
        context.language.transition(optional.accepted, aux, null);

        return context.segment(initial, accepted);
    }

    private NSegment at_least_n_times(NContext context, int count) {
        throw new UnsupportedOperationException();  // TODO
    }

    private NSegment exact_n_times(NContext context, int count) {
        var initial = context.language.state();
        var accepted = initial;

        for (int i = 0; i < count; i++) {
            if (separator != null) {
                throw new UnsupportedOperationException();  // TODO
            }

            var segment = content.build(context);

            context.language.transition(accepted, segment.initial, null);

            accepted = segment.accepted;
        }

        return context.segment(initial, accepted);
    }

    private NSegment at_least_but_no_more_times(NContext context, int min, int max) {
        throw new UnsupportedOperationException();  // TODO
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
