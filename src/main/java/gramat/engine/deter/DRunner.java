package gramat.engine.deter;

import gramat.common.TextException;
import gramat.engine.*;
import gramat.engine.actions.Action;
import gramat.engine.actions.ActionExecutor;
import gramat.engine.checks.ControlStack;
import gramat.engine.symbols.SymbolWild;

public class DRunner implements Runner {

    private final Input input;
    private final ActionExecutor executor;
    private final ControlStack controlStack;

    public DRunner(Input input, ActionExecutor executor) {
        this.input = input;
        this.executor = executor;
        this.controlStack = new ControlStack();
    }

    @Override
    public char getChar() {
        return input.peek();
    }

    @Override
    public ControlStack getControlStack() {
        return controlStack;
    }

    public DState eval(DState initial) {
        DState state = initial;

        while(state != null) {
            // find matching transition
            DTransition wildTransition = null;
            DTransition nextTransition = null;
            for (var transition : state.transitions) {
                if (transition.check == null || transition.check.test(controlStack)) {
                    if (transition.symbol instanceof SymbolWild) {
                        wildTransition = transition;
                    }
                    else if (transition.symbol.matches(this)) {
                        nextTransition = transition;
                        break;
                    }
                }
            }

            // fallback on wild transition
            if (nextTransition == null && wildTransition != null) {
                nextTransition = wildTransition;
            }

            // halt
            if (nextTransition == null) {
                break;
            }

            // apply check
            if (nextTransition.check != null) {
                nextTransition.check.apply(controlStack);
            }

            // execute actions
            for (var action : nextTransition.actions) {
                execute(action);
            }

            // go next state
            state = nextTransition.target;
            input.move();
        }

        if (controlStack.isActive()) {
            throw new TextException("Unexpected end", input.getLocation());
        }

        return state;
    }

    private void execute(Action action) {
        if (!executor.run(action)) {
            throw new TextException("Action cannot be executed: " + action, input.getLocation());
        }
    }

}
