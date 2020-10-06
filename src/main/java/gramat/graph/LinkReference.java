package gramat.graph;

import gramat.actions.ActionStore;

public class LinkReference extends Link {
    public final String reference;
    public LinkReference(Node source, Node target, ActionStore beforeActions, ActionStore afterActions, String reference) {
        super(source, target, beforeActions, afterActions);
        this.reference = reference;
    }
    @Override
    public String toString() {
        return source.id + "->" + target.id + " : " + reference;
    }
}
