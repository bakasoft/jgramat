package gramat.engine.deter;

import gramat.common.TextException;
import gramat.engine.*;

import java.util.Objects;
import java.util.Stack;

public class DRunner {

    private final Input input;
    private final ActionExecutor executor;
    private final Stack<Badge> badgeStack;

    public DRunner(Input input, ActionExecutor executor) {
        this.input = input;
        this.executor = executor;
        this.badgeStack = new Stack<>();
    }

    public DState eval(DState initial) {
        DState state = initial;

        while(state != null) {
            // peek current values
            var symbol = input.peek();
            var badge = badgeStack.isEmpty() ? null : badgeStack.peek();

            // find matching transition
            DTransition wildTransition = null;
            DTransition nextTransition = null;
            for (var transition : state.transitions) {
                if (transition.badge == null || Objects.equals(transition.badge, badge)) {
                    if (transition.symbol instanceof SymbolWild) {
                        wildTransition = transition;
                    }
                    else if (transition.symbol.matches(symbol)) {
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

            // execute actions
            for (var action : nextTransition.actions) {
                execute(action);
            }

            // go next state
            state = nextTransition.target;
            input.move();
        }

        if (badgeStack.size() > 0) {
            throw new TextException("Unexpected end", input.getLocation());
        }

        return state;
    }

    private void execute(Action action) {
        if (action instanceof BadgeActionPush) {
            var badge = ((BadgeActionPush) action).badge;

            badgeStack.push(badge);
        }
        else if (action instanceof BadgeActionPop) {
            badgeStack.pop();
        }
        else if (!executor.run(action)) {
            throw new TextException("Action cannot be executed: " + action, input.getLocation());
        }
    }

}
