package gramat.epsilon;

import java.util.concurrent.atomic.AtomicInteger;

public class Action {
    private static final AtomicInteger nextId = new AtomicInteger(1);

    public final int id;

    public Action() {
        id = nextId.getAndIncrement();
    }

}
