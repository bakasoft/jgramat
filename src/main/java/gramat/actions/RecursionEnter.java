package gramat.actions;

import gramat.badges.Badge;
import gramat.eval.Context;

public class RecursionEnter extends Action {

    public final Badge badge;

    public RecursionEnter(Badge badge) {
        this.badge = badge;
    }

    @Override
    public void run(Context context) {
        context.heap.push(badge);
    }

    @Override
    public String toString() {
        return String.format("enter(%s)", badge);
    }
}
