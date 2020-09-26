package gramat.proto;

import gramat.actions.Action;

import java.util.ArrayList;
import java.util.List;

abstract public class Edge {

    public final Vertex source;
    public final Vertex target;

    public final List<Action> beforeActions;
    public final List<Action> afterActions;

    public Edge(Vertex source, Vertex target) {
        this.source = source;
        this.target = target;
        this.beforeActions = new ArrayList<>();
        this.afterActions = new ArrayList<>();
    }

}
