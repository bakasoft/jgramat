package gramat.graph;

import java.util.Objects;

public class Plug extends Join {

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
    private final Node node;

    private Plug(int type, Link link, Node node) {
        super(link.token, link.beforeActions, link.afterActions);
        this.type = type;
        this.node = node;
    }

    public Node getNode() {
        return Objects.requireNonNull(node);
    }

}
