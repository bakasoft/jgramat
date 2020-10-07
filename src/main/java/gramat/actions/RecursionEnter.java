package gramat.actions;

import gramat.badges.Badge;
import gramat.eval.Context;

import java.util.List;
import java.util.Objects;

import static gramat.util.Validations.tryCastAndTest;

public class RecursionEnter extends Action {

    public final Badge badge;

    public RecursionEnter(Badge badge) {
        this.badge = badge;
    }

    @Override
    public boolean contains(Action other) {
        return tryCastAndTest(
                RecursionEnter.class,
                other,
                a -> Objects.equals(this.badge, a.badge)
        );
    }

    @Override
    public void run(Context context) {
        context.heap.push(badge);
    }

    @Override
    public List<String> getArguments() {
        return List.of(badge.toString());
    }
}
