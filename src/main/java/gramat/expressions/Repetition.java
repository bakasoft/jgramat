package gramat.expressions;

import gramat.engine.nodet.NCompiler;
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
    public NState build(NCompiler compiler, NState initial) {
        var min = (minimum != null ? minimum : 0);
        var max = (maximum != null ? maximum : 0);

        if (min < 0 || max < 0) {
            throw new RuntimeException("invalid values");
        }

        if (min == 0 && max == 0) {
            if (separator == null) {
                return zero_or_more_no_separator(compiler, initial);
            }
            else {
                return zero_or_more_with_separator(compiler, initial);
            }
        }
        else if (min == 1 && max == 0) {
            if (separator == null) {
                return one_or_more_no_separator(compiler, initial);
            }
            else {
                return one_or_more_with_separator(compiler, initial);
            }
        }
        else if (min > 1 && max == 0) {
            return at_least_n_times(compiler, initial, min);
        }
        else if (min == max) {
            return exact_n_times(compiler, initial, min);
        }
        else {
            return at_least_but_no_more_times(compiler, initial, min, max);
        }
    }

    private NState zero_or_more_no_separator(NCompiler compiler, NState initial) {
        var accepted = compiler.lang.newState();

        // zero times
        compiler.lang.newEmptyTransition(initial, accepted);

        // one time
        var contentStart = compiler.lang.newState();
        var contentAccepted = content.build(compiler, contentStart);
        compiler.lang.newEmptyTransition(initial, contentStart);
        compiler.lang.newEmptyTransition(contentAccepted, accepted);

        // more times
        compiler.lang.newEmptyTransition(contentAccepted, contentStart);

        return accepted;
    }

    private NState zero_or_more_with_separator(NCompiler compiler, NState initial) {
        // zero times
        var accepted = compiler.lang.newState();
        compiler.lang.newEmptyTransition(initial, accepted);

        // one time
        var contentStart = compiler.lang.newState();
        var contentAccepted = content.build(compiler, contentStart);
        compiler.lang.newEmptyTransition(initial, contentStart);
        compiler.lang.newEmptyTransition(contentAccepted, accepted);

        // more times
        var separatorAccepted = separator.build(compiler, contentAccepted);
        compiler.lang.newEmptyTransition(separatorAccepted, contentStart);

        return accepted;
    }

    private NState one_or_more_no_separator(NCompiler compiler, NState initial) {
        var accepted = compiler.lang.newState();

        // one time
        var contentStart = compiler.lang.newState();
        var contentAccepted = content.build(compiler, contentStart);
        compiler.lang.newEmptyTransition(initial, contentStart);
        compiler.lang.newEmptyTransition(contentAccepted, accepted);

        // more times
        compiler.lang.newEmptyTransition(contentAccepted, contentStart);

        return accepted;
    }

    private NState one_or_more_with_separator(NCompiler compiler, NState initial) {
        var accepted = compiler.lang.newState();

        // one time
        var contentStart = compiler.lang.newState();
        var contentAccepted = content.build(compiler, contentStart);
        compiler.lang.newEmptyTransition(initial, contentStart);
        compiler.lang.newEmptyTransition(contentAccepted, accepted);

        // more times
        var separatorAccepted = separator.build(compiler, contentAccepted);
        compiler.lang.newEmptyTransition(separatorAccepted, contentStart);

        return accepted;
    }

    private NState at_least_n_times(NCompiler compiler, NState initial, int count) {
        throw new UnsupportedOperationException();  // TODO
    }

    private NState exact_n_times(NCompiler compiler, NState initial, int count) {
        var last = initial;

        for (var i = 0; i < count; i++) {
            if (i > 0 && separator != null) {
                last = separator.build(compiler, last);
            }

            last = content.build(compiler, last);
        }

        return last;
    }

    private NState at_least_but_no_more_times(NCompiler compiler, NState initial, int min, int max) {
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
