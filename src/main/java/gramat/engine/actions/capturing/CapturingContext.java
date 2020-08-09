package gramat.engine.actions.capturing;

import gramat.engine.Input;
import gramat.tools.Debug;

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
        Debug.log("Push assembler", +1);
        assemblerStack.add(new ValueAssembler());
    }

    public ValueAssembler peekAssembler() {
        if (assemblerStack.isEmpty()) {
            throw new RuntimeException();
        }

        return assemblerStack.peek();
    }

    public ValueAssembler popAssembler() {
        Debug.log(-1, "Pop assembler");

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

        Debug.log("Postponed action: " + action);

        future.append(action);
        return action;
    }

    public void enqueue(CapturingAction action) {
        Debug.log("Enqueued action: " + action);

        future.append(action);
    }

    public <T extends CapturingAction> T dequeue(Class<T> type) {
        var action = present.removeLast(type);

        if (action == null) {
            action = future.removeLast(type);
        }

        if (action == null) {
            return null;
        }

        Debug.log("Dequeued action: " + action);
        return action;
    }

    public void flushFuture() {
        var actions = present.removeAll();

        present.appendAll(future.removeAll());

        for (var action : actions) {
            Debug.log("Execute action: " + action);

            action.run(this);
        }
    }
}
