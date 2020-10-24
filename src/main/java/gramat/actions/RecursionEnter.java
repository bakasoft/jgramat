package gramat.actions;

import gramat.badges.Badge;
import gramat.badges.BadgeToken;
import gramat.eval.Context;

import java.util.List;
import java.util.Objects;

public class RecursionEnter implements Action {

    public final BadgeToken badge;

    public RecursionEnter(BadgeToken badge) {
        this.badge = badge;
    }

    @Override
    public void run(Context context) {
        context.heap.push(badge);
    }

    @Override
    public String getName() {
        return "enter";
    }

    @Override
    public List<String> getArguments() {
        return List.of(badge.token);
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
