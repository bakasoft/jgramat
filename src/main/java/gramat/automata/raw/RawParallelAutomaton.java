package gramat.automata.raw;

import gramat.automata.builder.AutomatonBuilder;
import gramat.automata.builder.Segment;
import gramat.automata.nondet.NAutomaton;
import gramat.automata.nondet.NLanguage;
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
    public NAutomaton build(NLanguage lang) {
        var start = lang.state();
        var accept = lang.state();
        var reject = lang.state();

        for (var item : items) {
            var am = item.build(lang);

            start.linkEmpty(am.start);

            am.accept.linkEmpty(accept);
            am.reject.linkEmpty(reject);
        }

        return lang.automaton(start, reject, accept);
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
