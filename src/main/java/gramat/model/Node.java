package gramat.model;

import gramat.pivot.XState;

import java.util.Arrays;
import java.util.Set;

public class Node {

    public final String id;
    public boolean wild;// TODO

    public Node(String id) {
        this.id = id;
    }

    public static String computeID(Set<Node> nodes) {
        var ids = nodes.stream().map(n -> n.id).toArray();

        Arrays.sort(ids);

        var uid = new StringBuilder();

        for (int i = 0; i < ids.length; i++) {
            if (i > 0) {
                uid.append("_");
            }
            uid.append(ids[i]);
        }

        return uid.toString();
    }

}
