package gramat.graph;

public class Plug {

    public static final int FROM_SOURCE_TO_TARGET = 1;
    public static final int FROM_SOURCE_TO_NODE = 2;
    public static final int FROM_NODE_TO_SOURCE = 3;
    public static final int FROM_NODE_TO_TARGET = 4;
    public static final int FROM_TARGET_TO_NODE = 5;
    public static final int FROM_TARGET_TO_SOURCE = 6;

    public static Plug createFromSourceToNode(Link link, Node node) {
        return new Plug(FROM_SOURCE_TO_NODE, link, node);
    }

    public static Plug createFromSourceToTarget(Link link) {
        return new Plug(FROM_SOURCE_TO_TARGET, link, null);
    }

    public static Plug createFromNodeToSource(Link link, Node node) {
        return new Plug(FROM_NODE_TO_SOURCE, link, node);
    }

    public static Plug createFromNodeToTarget(Link link, Node node) {
        return new Plug(FROM_NODE_TO_TARGET, link, node);
    }

    public static Plug createFromTargetToNode(Link link, Node node) {
        return new Plug(FROM_TARGET_TO_NODE, link, node);
    }

    public static Plug createFromTargetToSource(Link link) {
        return new Plug(FROM_TARGET_TO_SOURCE, link, null);
    }

    public final int type;
    public final Node node;
    public final Link link;

    private Plug(int type, Link link, Node node) {
        this.type = type;
        this.link = link;
        this.node = node;
    }

}
