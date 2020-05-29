package gramat.automata.raw;

import gramat.automata.ndfa.NMachineBuilder;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

abstract public class RawAutomaton implements NMachineBuilder {

    abstract public RawAutomaton collapse();

    abstract public List<RawAutomaton> getChildren();

    public boolean isRecursive() {
        var control = new HashSet<RawAutomaton>();
        var queue = new LinkedList<RawAutomaton>();

        queue.addAll(getChildren());

        while (queue.size() > 0) {
            var expression = queue.remove();

            if (control.add(expression)) {
                if (expression == this) {
                    return true;
                }

                queue.addAll(expression.getChildren());
            }
        }

        return false;
    }

}