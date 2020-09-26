package gramat.nodes;

import gramat.Context;
import gramat.actions.Action;
import gramat.actions.ActionList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

abstract public class Node {

    private ActionList preActions;
    private ActionList postActions;

    final public void eval(Context context) {
        if (preActions != null) {
            preActions.runAll(context);
        }

        eval_impl(context);

        if (postActions != null) {
            postActions.runAll(context);
        }
    }

    final public ActionList getPreActions() {
        if (preActions == null) {
            preActions = new ActionList();
        }
        return preActions;
    }

    final public ActionList getPostActions() {
        if (postActions == null) {
            postActions = new ActionList();
        }
        return postActions;
    }

    abstract protected void eval_impl(Context context);

    abstract public boolean test(char value);

    abstract public Node collapse(NodeContext context);

    abstract public Node compile(NodeContext context);

    abstract public Node stack(Node other);

    abstract public List<NodeVertex> toVertices();

    abstract public List<Node> getNodes();

    abstract public boolean isOptional();

    abstract public Node shallowCopy();

    final public void addActionsFrom(Node node) {
        addPreActionsFrom(node);
        addPostActionsFrom(node);
    }

    final public void addPreActionsFrom(Node node) {
        if (node.preActions != null) {
            getPreActions().addAll(node.preActions);
        }
    }

    final public void addPostActionsFrom(Node node) {
        if (node.postActions != null) {
            getPostActions().addAll(node.postActions);
        }
    }

    final public void clearActions() {
        if (preActions != null) {
            preActions.clear();
            preActions = null;
        }

        if (postActions != null) {
            postActions.clear();

            postActions = null;
        }
    }

    final public void addPreAction(Action action) {
        getPreActions().add(action);
    }

    final public void addPostAction(Action action) {
        getPostActions().add(action);
    }

    final public boolean isRecursive() {
        var control = new HashSet<Node>();
        var queue = new LinkedList<>(getNodes());

        while (queue.size() > 0) {
            var expression = queue.remove();

            if (expression == this) {
                return true;
            }

            if (control.add(expression)) {
                queue.addAll(expression.getNodes());
            }
        }

        return false;
    }

    public static List<Node> tryStackNodes(List<Node> left, List<Node> right) {
        var size = left.size();
        if (size != right.size()) {
            return null;
        }
        var newNodes = new ArrayList<Node>();

        for (int i = 0; i < size; i++) {
            var leftNode = left.get(i);
            var rightNode = right.get(i);
            var stackedNode = leftNode.stack(rightNode);

            if (stackedNode == null) {
                return null;
            }

            newNodes.add(stackedNode);
        }

        return newNodes;
    }

    public boolean hasActions() {
        return preActions != null && postActions != null && preActions.size() > 0 && postActions.size() > 0;
    }
}
