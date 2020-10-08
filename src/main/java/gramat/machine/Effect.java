package gramat.machine;

import gramat.actions.Action;
import gramat.util.Chain;

public class Effect {

    public final State target;
    public final Chain<Action> before;
    public final Chain<Action> after;

    public Effect(State target) {
        this(target, null, null);
    }

    public Effect(State target, Chain<Action> before, Chain<Action> after) {
        this.target = target;
        this.before = before;
        this.after = after;
    }
}
