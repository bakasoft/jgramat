package gramat.scheme.common.actions;

import gramat.scheme.common.badges.BadgeToken;
import gramat.eval.EvalContext;

import java.util.Objects;

public class RecursionEnter implements ActionRecursive {

    public final BadgeToken badge;

    public RecursionEnter(BadgeToken badge) {
        this.badge = badge;
    }

    @Override
    public BadgeToken getBadge() {
        return badge;
    }

    @Override
    public void run(EvalContext context) {
        context.heap.push(badge);
    }

    @Override
    public String toString() {
        return String.format("enter(%s)", badge);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecursionEnter that = (RecursionEnter) o;
        return Objects.equals(badge, that.badge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(badge);
    }
}
