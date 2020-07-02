package gramat.expressions;

import gramat.engine.nodet.NBuilder;
import gramat.engine.nodet.NState;

import java.util.List;

public class Repetition extends Expression {

    private final Expression content;
    private final Expression separator;
    private final Integer minimum;
    private final Integer maximum;

    public Repetition(Expression content, Expression separator, Integer minimum, Integer maximum) {
        this.content = content;
        this.separator = separator;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public NState build(NBuilder builder, NState initial) {
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

    private NState zero_or_more_no_separator(NBuilder builder, NState initial) {
        // one time
        var accepted = content.build(builder, initial);

        // zero times
        builder.newEmptyTransition(initial, accepted);

        // more times
        builder.newEmptyTransition(accepted, initial);

        return accepted;
    }

    private NState zero_or_more_with_separator(NBuilder builder, NState initial) {
        // one time
        var accepted = content.build(builder, initial);

        // zero times
        builder.newEmptyTransition(initial, accepted);

        // more times
        var aux = separator.build(builder, accepted);
        builder.newEmptyTransition(aux, initial);

        return accepted;
    }

    private NState one_or_more_no_separator(NBuilder builder, NState initial) {
        // one time
        var accepted = content.build(builder, initial);

        // more times
        var aux = content.build(builder, accepted);
        builder.newEmptyTransition(aux, accepted);

        return accepted;
    }

    private NState one_or_more_with_separator(NBuilder builder, NState initial) {
        // one time
        var accepted = content.build(builder, initial);

        // more times
        var aux1 = separator.build(builder, accepted);
        var aux2 = content.build(builder, aux1);
        builder.newEmptyTransition(aux2, accepted);

        return accepted;
    }

    private NState at_least_n_times(NBuilder builder, NState initial, int count) {
        throw new UnsupportedOperationException();  // TODO
    }

    private NState exact_n_times(NBuilder builder, NState initial, int count) {
        var last = initial;

        for (var i = 0; i < count; i++) {
            if (i > 0 && separator != null) {
                last = separator.build(builder, last);
            }

            last = content.build(builder, last);
        }

        return last;
    }

    private NState at_least_but_no_more_times(NBuilder builder, NState initial, int min, int max) {
        throw new UnsupportedOperationException();  // TODO
    }

    @Override
    public List<Expression> getChildren() {
        if (separator != null) {
            return List.of(content, separator);
        }
        return List.of(content);
    }
}
