package gramat.automata.raw;

import gramat.automata.State;
import gramat.util.ListTool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class RawParallelAutomaton extends RawCompositeAutomaton {

    public RawParallelAutomaton() {
        super(new ArrayList<>());
    }

    public RawParallelAutomaton(List<RawAutomaton> items) {
        super(items);
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

    @Override
    public State compile(State s0) {
        var sF = new State();

        compile(s0, sF);

        return sF;
    }

    @Override
    public void compile(State s0, State sF) {
        for (var automaton : items) {
            automaton.compile(s0, sF);
        }

        sF.makeAccepted();
    }

    @Override
    public Character getSingleCharOrNull() {
        Character result = null;

        for (var item : items) {
            var ch = item.getSingleCharOrNull();

            if (ch == null) {
                return null;
            }
            else if (result == null) {
                result = ch;
            }
            else if (result != ch) {
                return null;
            }
        }

        return result;
    }

    @Override
    protected RawAutomaton removeFirstChar() {
        var newItems = new ArrayList<>(items);

        for (var i = 0; i < newItems.size(); i++) {
            newItems.set(i, newItems.get(i).removeFirstChar());
        }

        return new RawParallelAutomaton(newItems);
    }
}
