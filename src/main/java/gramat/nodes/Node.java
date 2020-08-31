package gramat.nodes;

import gramat.Context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public interface Node {

    void eval(Context context);

    boolean test(char value);

    Node collapse(NodeContext context);

    Node compile(NodeContext context);

    Node tryStack(Node other);

    List<NodeVertex> toVertices();

    List<Node> getNodes();

    boolean isOptional();

    default boolean isRecursive() {
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

    static List<Node> tryStackNodes(List<Node> left, List<Node> right) {
        var size = left.size();
        if (size != right.size()) {
            return null;
        }
        var newNodes = new ArrayList<Node>();

        for (int i = 0; i < size; i++) {
            var leftNode = left.get(i);
            var rightNode = right.get(i);
            var stackedNode = leftNode.tryStack(rightNode);

            if (stackedNode == null) {
                return null;
            }

            newNodes.add(stackedNode);
        }

        return newNodes;
    }

}
