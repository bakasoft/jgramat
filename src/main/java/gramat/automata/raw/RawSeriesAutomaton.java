package gramat.automata.raw;

import gramat.automata.State;
import gramat.util.ListTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RawSeriesAutomaton extends RawCompositeAutomaton {

    public RawSeriesAutomaton(List<RawAutomaton> items) {
        super(items);
    }

    public RawSeriesAutomaton() {
        super(new ArrayList<>());
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

        return join_literals(join_series(this)).collapse();
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

    @Override
    public State compile(State s0) {
        var s = s0;

        for (var automaton : items) {
            s = automaton.compile(s);
        }

        s.makeAccepted();

        return s;
    }

    @Override
    public void compile(State s0, State sF) {
        var s = s0;

        for (var i = 0; i < items.size(); i++) {
            var a = items.get(i);

            if (i == items.size() - 1) {
                a.compile(s, sF);
            }
            else {
                s = a.compile(s);
            }
        }

        sF.makeAccepted();
    }

    @Override
    public Character getSingleCharOrNull() {
        if (items.isEmpty()) {
            return null;
        }

        return items.get(0).getSingleCharOrNull();
    }

    @Override
    protected RawAutomaton removeFirstChar() {
        if (items.isEmpty()) {
            return new RawNopAutomaton();
        }

        var newItems = new ArrayList<>(items);

        var item0 = newItems.get(0);

        item0 = item0.removeFirstChar();

        newItems.set(0, item0);

        return new RawSeriesAutomaton(newItems);
    }
}
