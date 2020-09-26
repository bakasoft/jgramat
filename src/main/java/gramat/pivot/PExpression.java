package gramat.pivot;

import gramat.actions.Action;
import gramat.actions.ActionList;
import gramat.nodes.Node;

abstract public class PExpression {

    abstract public PExpression compile(PContext context);

    abstract public PExpression shallowCopy();

    private final ActionList preActions;
    private final ActionList postActions;

    public PExpression() {
        preActions = new ActionList();
        postActions = new ActionList();
    }

    final public void addActionsFrom(PExpression expression) {
        addPreActionsFrom(expression);
        addPostActionsFrom(expression);
    }

    final public void addPreActionsFrom(PExpression expression) {
        preActions.addAll(expression.preActions);
    }

    final public void addPostActionsFrom(PExpression expression) {
        postActions.addAll(expression.postActions);
    }

    final public void clearActions() {
        preActions.clear();
        postActions.clear();
    }

    final public void addPreAction(Action action) {
        preActions.add(action);
    }

    final public void addPostAction(Action action) {
        postActions.add(action);
    }
}
