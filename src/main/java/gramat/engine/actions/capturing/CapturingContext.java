package gramat.engine.actions.capturing;

import gramat.engine.actions.Action;
import gramat.engine.Input;

import java.util.Stack;

public class CapturingContext {

    public final Input input;

    private final Stack<ValueAssembler> assemblerStack;

    public CapturingContext(Input input) {
        this.input = input;

        this.assemblerStack = new Stack<>();

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

}
