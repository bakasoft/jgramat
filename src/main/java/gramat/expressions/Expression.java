package gramat.expressions;

import gramat.engine.nodet.NBuilder;
import gramat.engine.nodet.NMachine;
import gramat.engine.nodet.NState;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

abstract public class Expression {

    abstract public NState build(NBuilder builder, NState initial);

    abstract public List<Expression> getChildren();

    public NMachine machine(NBuilder builder, NState initial) {
        var context = new NBuilder(builder);
        var accepted = build(context, initial);

        // freeze states & transitions
        var machineStates = context.states.copy();
        var machineTransitions = context.transitions.copy();

        // be sure initial state is in the states set
        machineStates.add(initial);

        return new NMachine(context.root, initial, accepted, machineStates, machineTransitions);
    }

    public boolean isRecursive() {
        var control = new HashSet<Expression>();
        var queue = new LinkedList<Expression>();

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
