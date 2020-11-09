package gramat.scheme.core.actions;

import gramat.scheme.core.badges.BadgeToken;
import gramat.eval.EvalContext;

import java.util.Objects;

public class RecursionExit implements ActionRecursive {

    public final BadgeToken badge;

    public RecursionExit(BadgeToken badge) {
        this.badge = badge;
    }

    @Override
    public BadgeToken getBadge() {
        return badge;
    }

    @Override
    public void run(EvalContext context) {
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
