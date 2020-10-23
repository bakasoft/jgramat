package gramat.graph;

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

}
