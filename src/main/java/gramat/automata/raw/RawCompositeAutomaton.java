package gramat.automata.raw;

import gramat.automata.raw.units.RawNopAutomaton;

import java.util.List;

abstract public class RawCompositeAutomaton extends RawAutomaton {

    protected final List<RawAutomaton> items;

    public RawCompositeAutomaton(List<RawAutomaton> items) {
        this.items = items;
    }

    public void addAutomaton(RawAutomaton automaton) {
        this.items.add(automaton);
    }

    protected static void collapseAll(List<RawAutomaton> items) {
        // collapse all items
        for (int i = items.size() - 1; i >= 0; i--) {
            var item = items.get(i).collapse();

            if (item instanceof RawNopAutomaton) {
                items.remove(i);
            }
            else {
                items.set(i, item);
            }
        }
    }

}
