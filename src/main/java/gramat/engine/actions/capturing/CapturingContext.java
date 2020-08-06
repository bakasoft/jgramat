package gramat.engine.actions.capturing;

import gramat.engine.Input;

import java.util.Stack;
import java.util.function.Predicate;

public class CapturingContext {

    public final Input input;

    private final Stack<ValueAssembler> assemblerStack;

    public final CapturingQueue future;
    public final CapturingQueue present;


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

    public CapturingAction tryPostpone(Predicate<CapturingAction> condition) {
        var action = present.removeLast(condition);

        if (action == null) {
            return null;
        }

        future.enqueue(action);
        return action;
    }

    public void flushFuture() {
        var actions = present.dequeueAll();

        present.enqueueAll(future.dequeueAll());

        for (var action : actions) {
            action.run(this);
        }
    }

}
