package gramat.automata.raw;

import gramat.automata.ndfa.NAutomaton;
import gramat.automata.ndfa.Language;
import gramat.automata.raw.units.RawNopAutomaton;
import gramat.util.ListTool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RawParallelAutomaton extends RawCompositeAutomaton {

    public RawParallelAutomaton() {
        super(new ArrayList<>());
    }

    public RawParallelAutomaton(List<RawAutomaton> items) {
        super(items);
    }

    @Override
    public NAutomaton build(Language lang) {
        var initial = lang.state();
        var accepted = lang.state();

        for (var item : items) {
            var am = item.build(lang);

            lang.transition(initial, am.initial, null);
            lang.transition(am.accepted, accepted, null);
        }

        return lang.automaton(initial, accepted);
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

        // TODO join prefixes
        return join_parallels(this);
    }

    private static RawAutomaton join_parallels(RawParallelAutomaton automaton) {
        var items = new ArrayList<RawAutomaton>();
        var different = new AtomicBoolean(false);

        ListTool.collapse(
                automaton.items,
                RawParallelAutomaton.class,
                items::add,
                is ->  {
                    different.set(true);
                    items.addAll(is);
                });

        if (different.get()) {
            return new RawParallelAutomaton(items);
        }

        return automaton;
    }

}
