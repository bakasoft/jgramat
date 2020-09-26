package gramat.pivot;

import gramat.actions.Action;

public class XActionBlock {

    public final XState initial;
    public final Action before;
    public final XState accepted;
    public final Action after;

    public XActionBlock(XState initial, Action before, XState accepted, Action after) {
        this.initial = initial;
        this.before = before;
        this.accepted = accepted;
        this.after = after;
    }
}
