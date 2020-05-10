package gramat.automata.raw;

import gramat.automata.nondet.NLanguage;
import gramat.automata.nondet.NState;
import gramat.util.ListTool;

import java.util.ArrayList;
import java.util.List;


public class RawSeriesAutomaton extends RawCompositeAutomaton {

    public RawSeriesAutomaton(List<RawAutomaton> items) {
        super(items);
    }

    public RawSeriesAutomaton() {
        super(new ArrayList<>());
    }

    @Override
    public NState build(NLanguage lang, NState start) {
        var last = start;
        for (var item : items) {
            last = item.build(lang, last);
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
