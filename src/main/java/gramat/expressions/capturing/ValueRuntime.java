package gramat.expressions.capturing;

import gramat.engine.actions.Action;
import gramat.engine.actions.ActionExecutor;
import gramat.engine.Input;

import java.util.Stack;

public class ValueRuntime implements ActionExecutor {

    public final Input input;

    private final Stack<ValueAssembler> assemblerStack;

    public ValueRuntime(Input input) {
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

    @Override
    public boolean run(Action action) {
        if (action instanceof ValueAction) {
            var valueAction = (ValueAction) action;

            valueAction.run(this);

            return true;
        }

        return false;
    }
}
