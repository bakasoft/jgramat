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

    public NMachine buildOnce(NBuilder builder, String name) {
        var machine = builder.getMachine(name);

        if (machine == null) {
            // create stand-alone machine
            machine = new NMachine(name, builder.root.newState(), builder.root.newState());

            // make it public so recursive builds can use it
            builder.addMachine(machine);

            // build expression inside machine (can be recursive)
            var aux = build(builder, machine.initial);

            // connect with the public machine
            builder.root.newEmptyTransition(aux, machine.accepted);
        }

        return machine;
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
