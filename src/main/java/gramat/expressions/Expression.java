package gramat.expressions;

import gramat.engine.nodet.NCompiler;
import gramat.engine.nodet.NState;
import gramat.expressions.capturing.CData;

import java.util.ArrayList;
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

    public List<CData> findData() {
        var result = new ArrayList<CData>();
        var control = new HashSet<Expression>();
        var queue = new LinkedList<Expression>();

        queue.add(this);

        while (queue.size() > 0) {
            var expression = queue.remove();

            if (control.add(expression)) {
                if (expression instanceof CData) {
                    result.add((CData)expression);
                }
                else {
                    // enqueue children
                    queue.addAll(expression.getChildren());
                }
            }
        }

        return result;
    }
}
