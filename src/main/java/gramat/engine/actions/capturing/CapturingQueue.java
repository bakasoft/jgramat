package gramat.engine.actions.capturing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CapturingQueue {

    private final LinkedList<CapturingAction> queue;

    public CapturingQueue() {
        this.queue = new LinkedList<>();
    }

    public void append(CapturingAction action) {
        queue.add(action);
    }

    public void appendAll(List<CapturingAction> actions) {
        queue.addAll(actions);
    }

    public <T extends CapturingAction> T removeLast(Class<T> type) {
        for (int i = queue.size() - 1; i >= 0; i--) {
            if (type.isInstance(queue.get(i))) {
                var action = queue.remove(i);

                return type.cast(action);
            }
        }

        return null;
    }

    public List<CapturingAction> removeAll() {
        var result = new ArrayList<>(queue);
        queue.clear();
        return result;
    }
}
