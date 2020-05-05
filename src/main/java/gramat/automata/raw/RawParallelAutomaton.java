package gramat.automata.raw;

import gramat.automata.builder.AutomatonBuilder;
import gramat.automata.builder.SegmentBuilder;
import gramat.automata.builder.StateBuilder;
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
    public SegmentBuilder build(AutomatonBuilder builder, StateBuilder s0) {
        var sF = builder.createState();
        var segment = builder.createSegment(s0, sF);

        sF.makeAccepted();

        for (var item : items) {
            var sub = item.build(builder, s0);

            builder.replace(sub.end, sF);
        }

        return segment;
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
            return new RawParallelAutomaton(items).collapse();
        }

        return automaton;
    }

}
