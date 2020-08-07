package gramat.engine.actions.capturing;

import gramat.engine.Input;
import gramat.engine.actions.capturing.catalog.ObjectReject;

import java.util.Stack;

public class CapturingContext {

    public final Input input;

    private final Stack<ValueAssembler> assemblerStack;

    private final CapturingQueue future;
    private final CapturingQueue present;


    public CapturingContext(Input input) {
        this.input = input;
        this.assemblerStack = new Stack<>();
        this.future = new CapturingQueue();
        this.present = new CapturingQueue();

        pushAssembler();
    }

    public void pushAssembler() {
        assemblerStack.add(new ValueAssembler());
    }

    public ValueAssembler peekAssembler() {
        if (assemblerStack.isEmpty()) {
            throw new RuntimeException();
        }

        return assemblerStack.peek();
    }

    public ValueAssembler popAssembler() {
        if (assemblerStack.isEmpty()) {
            throw new RuntimeException();
        }

        return assemblerStack.pop();
    }

    public <T extends CapturingAction> T tryPostpone(Class<T> type) {
        var action = present.removeLast(type);

        if (action == null) {
            return null;
        }

        future.append(action);
        return action;
    }

    public void enqueue(CapturingAction action) {
        future.append(action);
    }

    public <T extends CapturingAction> T dequeue(Class<T> type) {
        return present.removeLast(type);
    }

    public void flushFuture() {
        var actions = present.removeAll();

        present.appendAll(future.removeAll());

        for (var action : actions) {
            action.run(this);
        }
    }
}
