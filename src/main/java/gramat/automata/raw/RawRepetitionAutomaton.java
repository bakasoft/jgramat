package gramat.automata.raw;

import gramat.automata.ndfa.NContext;


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
    public RawAutomaton collapse() {
        return new RawRepetitionAutomaton(
                content.collapse(),
                separator != null ? separator.collapse() : null,
                minimum,
                maximum
        );
    }

    @Override
    public void build(NContext context) {
        var min = (minimum != null ? minimum : 0);
        var max = (maximum != null ? maximum : 0);

        if (min < 0 || max < 0) {
            throw new RuntimeException("invalid values");
        }

        if (min == 0 && max == 0 && separator == null) {
            zero_or_more_no_separator(context);
        }
        else if (min == 0 && max == 0) {
            zero_or_more_with_separator(context);
        }
        else if (min == 1 && max == 0) {
            one_or_more(context);
        }
        else if (min > 1 && max == 0) {
            at_least_n_times(context, min);
        }
        else if (min == max) {
            exact_n_times(context, min);
        }
        else {
            at_least_but_no_more_times(context, min, max);
        }
    }

    private void zero_or_more_no_separator(NContext context) {
        var root = context.initialAccepted();

        var machine = context.subMachine(content);

        context.transitionNull(root, machine.initial);
        context.transitionNull(machine.accepted, root);
    }

    private void zero_or_more_with_separator(NContext context) {
        var initial = context.initialAccepted();
        var accepted = context.accepted();
        var cMachineMain = context.subMachine(content);

        context.transitionNull(initial, cMachineMain.initial);
        context.transitionNull(cMachineMain.accepted, accepted);

        var cMachineArc = context.subMachine(content);
        var sMachineArc = context.subMachine(separator);

        context.transitionNull(accepted, sMachineArc.initial);
        context.transitionNull(sMachineArc.accepted, cMachineArc.initial);
        context.transitionNull(cMachineArc.accepted, accepted);
    }

    private void one_or_more(NContext context) {
        var cMachine = context.subMachine(content);

        if (separator != null) {
            var sMachine = context.subMachine(separator);
            context.transitionNull(cMachine.accepted, sMachine.initial);
            context.transitionNull(sMachine.accepted, cMachine.initial);
        }
        else {
            context.transitionNull(cMachine.accepted, cMachine.initial);
        }

        context.initial(cMachine.initial);
        context.accepted(cMachine.accepted);
    }

    private void at_least_n_times(NContext context, int count) {
        throw new UnsupportedOperationException();  // TODO
    }

    private void exact_n_times(NContext context, int count) {
        throw new UnsupportedOperationException();  // TODO
    }

    private void at_least_but_no_more_times(NContext context, int min, int max) {
        throw new UnsupportedOperationException();  // TODO
    }
}
