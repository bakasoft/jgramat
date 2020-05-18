package gramat.eval;

import java.util.concurrent.atomic.AtomicInteger;

abstract public class Action {

    private static final AtomicInteger nextId = new AtomicInteger(1);

    abstract public void run(Evaluator evaluator);

    protected final String id;

    public Action() {
        id = String.valueOf(nextId.getAndIncrement());
    }

    abstract public String getDescription();

    @Override
    public String toString() {
        return getDescription() + " (#" + id + ")";
    }
}
