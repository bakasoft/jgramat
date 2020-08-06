package gramat.engine.deter;

import gramat.common.TextException;
import gramat.engine.*;
import gramat.engine.actions.Action;
import gramat.engine.actions.ActionExecutor;
import gramat.engine.checks.Check;
import gramat.engine.checks.ControlStack;
import gramat.engine.symbols.*;

public class DRunner {

    private final Input input;
    private final ActionExecutor executor;
    private final ControlStack controlStack;

    public DRunner(Input input, ActionExecutor executor) {
        this.input = input;
        this.executor = executor;
        this.controlStack = new ControlStack();
    }

    private static class SymbolResult {

        public final boolean wild;
        public final Check check;

        public SymbolResult(boolean wild, Check check) {
            this.wild = wild;
            this.check = check;
        }

    }

    private static final SymbolResult WILD_NO_CHECK = new SymbolResult(true, null);
    private static final SymbolResult MATCH_NO_CHECK = new SymbolResult(false, null);

    private SymbolResult testSymbol(Symbol symbol) {
        if (symbol instanceof SymbolWild) {
            return WILD_NO_CHECK;
        }
        else if (symbol instanceof SymbolChar) {
            var chr = ((SymbolChar)symbol).value;

            if (chr == input.peek()) {
                return MATCH_NO_CHECK;
            }
        }
        else if (symbol instanceof SymbolRange) {
            var begin = ((SymbolRange)symbol).begin;
            var end = ((SymbolRange)symbol).end;
            var current = input.peek();

            if (current >= begin && current <= end) {
                return MATCH_NO_CHECK;
            }
        }
        else if (symbol instanceof SymbolCheck) {
            var sch = (SymbolCheck)symbol;
            if (sch.check.test(controlStack)) {
                var result = testSymbol(sch.symbol);

                if (result != null) {
                    return new SymbolResult(result.wild, sch.check);
                }
            }
        }
        else {
            throw new RuntimeException("unsupported symbol class: " + symbol.getClass());
        }

        return null;
    }

    private static class TransitionResult {

        public final DState target;
        public final Action[] actions;
        public final Check check;

        private TransitionResult(DState target, Action[] actions, Check check) {
            this.target = target;
            this.actions = actions;
            this.check = check;
        }

    }

    private TransitionResult chooseNextTransition(DState state) {
        DTransition chosenTransition = null;
        SymbolResult symbolResult = null;

        if (state.transitions != null) {
            for (var transition : state.transitions) {
                var result = testSymbol(transition.symbol);

                if (result != null) {
                    chosenTransition = transition;
                    symbolResult = result;

                    if (!result.wild) {
                        break;
                    }
                }
            }
        }

        if (chosenTransition == null) {
            return null;
        }

        return new TransitionResult(chosenTransition.target, chosenTransition.actions, symbolResult.check);
    }

    public DState eval(DState initial) {
        DState state = initial;

        while(state != null) {
            var nextTransition = chooseNextTransition(state);

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
