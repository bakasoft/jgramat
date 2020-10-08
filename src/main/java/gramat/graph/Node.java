package gramat.graph;

import gramat.util.Chain;
import gramat.util.StringUtils;

import java.util.Comparator;

public class Node {

    public final String id;

    public boolean wild;

    public Node(String id, boolean wild) {
        this.id = id;
        this.wild = wild;
    }

    @Override
    public String toString() {
        return String.format("Node[%s]", id);
    }

    public static String computeID(Chain<Node> nodes) {
        var ids = nodes.map(n -> n.id);

        ids.sort(Comparator.naturalOrder());

        return StringUtils.join("_", ids);
    }

}
