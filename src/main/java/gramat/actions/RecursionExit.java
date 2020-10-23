package gramat.actions;

import gramat.badges.Badge;
import gramat.eval.Context;

import java.util.Objects;

public class RecursionExit implements Action {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecursionExit that = (RecursionExit) o;
        return Objects.equals(badge, that.badge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(badge);
    }
}
