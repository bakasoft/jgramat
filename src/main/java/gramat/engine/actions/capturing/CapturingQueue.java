package gramat.engine.actions.capturing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class CapturingQueue {

    private final LinkedList<CapturingAction> queue;

    public CapturingQueue() {
        this(new LinkedList<>());
    }

    private CapturingQueue(LinkedList<CapturingAction> queue) {
        this.queue = queue;
    }

    public void enqueue(CapturingAction action) {
        queue.add(action);
    }

    public List<CapturingAction> dequeueAll() {
        var result = new ArrayList<>(queue);
        queue.clear();
        return result;
    }

    public CapturingAction removeLast(Predicate<CapturingAction> condition) {
        for (int i = queue.size() - 1; i >= 0; i--) {
            if (condition.test(queue.get(i))) {
                return queue.remove(i);
            }
        }

        return null;
    }

    public void enqueueAll(List<CapturingAction> actions) {
        queue.addAll(actions);
    }
}
