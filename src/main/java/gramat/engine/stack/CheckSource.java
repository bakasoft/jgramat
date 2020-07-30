package gramat.engine.stack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CheckSource {

    private final ArrayList<ControlCheck> checks;

    public CheckSource() {
        this(new ArrayList<>());
    }

    private CheckSource(ArrayList<ControlCheck> checks) {
        this.checks = checks;
    }

    public List<ControlCheck> getItems() {
        return Collections.unmodifiableList(checks);
    }

    public ControlCheck push(String token) {
        for (var check : checks) {
            if (check instanceof PushCheck) {
                var push = (PushCheck) check;

                if (Objects.equals(token, push.token)) {
                    return push;
                }
            }
        }

        var push = new PushCheck(token);

        checks.add(push);

        return push;
    }

    public ControlCheck pop(String token) {
        for (var check : checks) {
            if (check instanceof PopCheck) {
                var pop = (PopCheck) check;

                if (Objects.equals(token, pop.token)) {
                    return pop;
                }
            }
        }

        var pop = new PopCheck(token);

        checks.add(pop);

        return pop;
    }

    public ControlCheck wild() {
        for (var check : checks) {
            if (check instanceof WildCheck) {
                return check;
            }
        }
        var wild = new WildCheck();

        checks.add(wild);

        return wild;
    }

    public ControlCheck empty() {
        for (var check : checks) {
            if (check instanceof EmptyCheck) {
                return check;
            }
        }
        var empty = new EmptyCheck();

        checks.add(empty);

        return empty;
    }

    public CheckSource copy() {
        return new CheckSource(new ArrayList<>(checks));
    }
}
