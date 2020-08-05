package gramat.expressions;

import gramat.engine.nodet.NCompiler;
import gramat.engine.nodet.NState;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

abstract public class Expression {

    abstract public NState build(NCompiler compiler, NState initial);

    abstract public List<Expression> getChildren();

    public boolean isRecursive() {
        var control = new HashSet<Expression>();
        var queue = new LinkedList<>(getChildren());

        while (queue.size() > 0) {
            var expression = queue.remove();

            if (expression == this) {
                return true;
            }

            if (control.add(expression)) {
                queue.addAll(expression.getChildren());
            }
        }

        return false;
    }
}
