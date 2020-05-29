package gramat.automata.raw;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NStateSet;

import java.util.Collections;
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
    public void build(NContext context, NStateSet initial, NStateSet accepted) {
        var min = (minimum != null ? minimum : 0);
        var max = (maximum != null ? maximum : 0);

        if (min < 0 || max < 0) {
            throw new RuntimeException("invalid values");
        }

        if (min == 0 && max == 0) {
            if (separator == null) {
                zero_or_more_no_separator(context, initial, accepted);
            }
            else {
                zero_or_more_with_separator(context, initial, accepted);
            }
        }
        else if (min == 1 && max == 0) {
            if (separator == null) {
                one_or_more_no_separator(context, initial, accepted);
            }
            else {
                one_or_more_with_separator(context, initial, accepted);
            }
        }
        else if (min > 1 && max == 0) {
            at_least_n_times(context, initial, accepted, min);
        }
        else if (min == max) {
            exact_n_times(context, initial, accepted, min);
        }
        else {
            at_least_but_no_more_times(context, initial, accepted, min, max);
        }
    }

    private void zero_or_more_no_separator(NContext context, NStateSet initial, NStateSet accepted) {
        content.build(context, initial, initial);

        accepted.add(initial);
    }

    private void zero_or_more_with_separator(NContext context, NStateSet initial, NStateSet accepted) {
        // => ini => acc : c
        //    acc -> aux : s
        //    aux -> acc : c
        var aux = new NStateSet();

        content.build(context, initial, accepted);
        separator.build(context, accepted, aux);
        content.build(context, aux, accepted);

        accepted.add(initial);
    }

    private void one_or_more_no_separator(NContext context, NStateSet initial, NStateSet accepted) {
        // -> ini => acc : c
        //    acc -> acc : c
        content.build(context, initial, accepted);
        content.build(context, accepted, accepted);
    }

    private void one_or_more_with_separator(NContext context, NStateSet initial, NStateSet accepted) {
        // -> ini => acc : c
        //    acc -> aux : s
        //    aux -> acc : c
        var aux = new NStateSet();

        content.build(context, initial, accepted);
        separator.build(context, accepted, aux);
        content.build(context, aux, accepted);
    }

    private void at_least_n_times(NContext context, NStateSet initial, NStateSet accepted, int count) {
        throw new UnsupportedOperationException();  // TODO
    }

    private void exact_n_times(NContext context, NStateSet initial, NStateSet accepted, int count) {
        var last = initial;

        for (int i = 0; i < count; i++) {
            var isLast = (i == count - 1);
            var next = isLast ? accepted : new NStateSet();

            if (separator != null) {
                throw new UnsupportedOperationException();  // TODO
            }

            content.build(context, last, next);

            last = next;
        }
    }

    private void at_least_but_no_more_times(NContext context, NStateSet initial, NStateSet accepted, int min, int max) {
        throw new UnsupportedOperationException();  // TODO
    }
}
