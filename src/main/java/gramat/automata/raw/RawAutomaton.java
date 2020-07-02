package gramat.automata.raw;

import gramat.epsilon.Builder;
import gramat.epsilon.State;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

abstract public class RawAutomaton {

    abstract public State build(Builder builder, State initial);

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