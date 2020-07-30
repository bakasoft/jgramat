package gramat.engine.control;

import java.util.*;

public class CheckSource implements Iterable<Check> {

    private final ArrayList<Check> checks;

    public CheckSource() {
        this(new ArrayList<>());
    }

    private CheckSource(ArrayList<Check> checks) {
        this.checks = checks;
    }

    public List<Check> getItems() {
        return Collections.unmodifiableList(checks);
    }

    public Check push(String token) {
        for (var check : checks) {
            if (check instanceof CheckPush) {
                var push = (CheckPush) check;

                if (Objects.equals(token, push.token)) {
                    return push;
                }
            }
        }

        var push = new CheckPush(token);

        checks.add(push);

        return push;
    }

    public Check pop(String token) {
        for (var check : checks) {
            if (check instanceof CheckPop) {
                var pop = (CheckPop) check;

                if (Objects.equals(token, pop.token)) {
                    return pop;
                }
            }
        }

        var pop = new CheckPop(token);

        checks.add(pop);

        return pop;
    }

    public Check getNull() {
        for (var check : checks) {
            if (check instanceof CheckNull) {
                return check;
            }
        }
        var wild = new CheckNull();

        checks.add(wild);

        return wild;
    }

    public Check getAny() {
        for (var check : checks) {
            if (check instanceof CheckAny) {
                return check;
            }
        }
        var any = new CheckAny();

        checks.add(any);

        return any;
    }

    public Check getClear() {
        for (var check : checks) {
            if (check instanceof CheckClear) {
                return check;
            }
        }
        var empty = new CheckClear();

        checks.add(empty);

        return empty;
    }

    public CheckSource copy() {
        return new CheckSource(new ArrayList<>(checks));
    }

    @Override
    public Iterator<Check> iterator() {
        return checks.iterator();
    }
}
