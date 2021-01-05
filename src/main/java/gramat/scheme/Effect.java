package gramat.scheme;

import gramat.scheme.common.actions.Action;

public class Effect {

    public final State target;
    public final Action[] before;
    public final Action[] after;

    public Effect(State target) {
        this(target, null, null);
    }

    public Effect(State target, Action[] before, Action[] after) {
        this.target = target;
        this.before = before;
        this.after = after;
    }
}
