package gramat.actions;

import gramat.badges.Badge;
import gramat.eval.Context;

public class RecursionExit extends Action {

    public final Badge badge;

    public RecursionExit(Badge badge) {
        this.badge = badge;
    }

    @Override
    public void run(Context context) {
        context.heap.pop(badge);
    }

    @Override
    public String toString() {
        return String.format("exit(%s)", badge);
    }
}
