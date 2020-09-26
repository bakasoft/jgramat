package gramat.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.BiConsumer;

public class Walker<T> {

    private final LinkedList<T> queue;
    private final HashSet<T> control;

    public Walker(T initial) {
        queue = new LinkedList<>();
        control = new HashSet<>();

        queue.add(initial);
    }

    public void walk(BiConsumer<T, Queue<T>> step) {
        while (queue.size() > 0) {
            var item = queue.remove();

            if (control.add(item)) {
                step.accept(item, queue);
            }
        }
    }

}
