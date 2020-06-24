package gramat.automata.raw;

import gramat.automata.ndfa.NContext;
import gramat.automata.ndfa.NSegment;
import gramat.automata.ndfa.NStateSet;
import gramat.automata.raw.units.RawLiteralAutomaton;
import gramat.automata.raw.units.RawNopAutomaton;
import gramat.epsilon.Builder;
import gramat.epsilon.State;
import gramat.util.ListTool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class RawSeriesAutomaton extends RawCompositeAutomaton {

    public RawSeriesAutomaton(List<RawAutomaton> items) {
        super(items);
    }

    public RawSeriesAutomaton() {
        super(new ArrayList<>());
    }

    @Override
    public List<RawAutomaton> getChildren() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public NSegment build(NContext context) {
        var initial = context.language.state();
        var accepted = initial;

        for (var item : items) {
            var segment = item.build(context);

            context.language.transition(accepted, segment.initial, null);

            accepted = segment.accepted;
        }

        return context.segment(initial, accepted);
    }

    @Override
    public State build(Builder builder, State initial) {
        var last = initial;

        for (var item : items) {
            last = item.build(builder, last);
        }

        return last;
    }

    @Override
    public RawAutomaton collapse() {
        collapseAll(items);

        if (items.isEmpty()) {
            return new RawNopAutomaton();
        }
        else if (items.size() == 1) {
            return items.get(0);
        }

        return join_literals(join_series(this));
    }

    private static RawAutomaton join_literals(RawSeriesAutomaton series) {
        var items = new ArrayList<RawAutomaton>();

        ListTool.collapse(
                series.items,
                RawStringAutomaton.class,
                items::add,
                strs -> {
                    StringBuilder str = new StringBuilder();

                    for (var s : strs) {
                        str.append(s.getStringValue());
                    }

                    items.add(new RawLiteralAutomaton(str.toString()));
                });

        return new RawSeriesAutomaton(items);
    }

    private static RawSeriesAutomaton join_series(RawSeriesAutomaton series) {
        var items = new ArrayList<RawAutomaton>();

        ListTool.collapse(
                series.items,
                RawSeriesAutomaton.class,
                items::add,
                items::addAll);

        return new RawSeriesAutomaton(items);
    }
}
