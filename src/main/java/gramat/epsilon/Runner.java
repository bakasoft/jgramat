package gramat.epsilon;

import java.util.*;

public class Runner {

    private final Input input;
    private final Stack<Token> tokenStack;

    public Runner(Input input) {
        this.input = input;
        this.tokenStack = new Stack<>();
    }

    public boolean run(State initial, State accepted) {
        boolean result = false;
        Set<State> states = Set.of(initial);

        while(input.alive()) {
            result = states.contains(accepted);

            states = tick(states);

            if (states.isEmpty()) {
                break;
            }

            input.move();
        }

        return result;
    }

    public Set<State> tick(Set<State> states) {
        var targets = new HashSet<State>();
        var symbol = input.peek();
        var transitions = new LinkedList<Transition>();
        var wildTrans = new ArrayList<Transition>();

        for (var state : states) {
            int targetCount0 = targets.size();

            transitions.addAll(state.transitions);

            while (transitions.size() > 0) {
                var transition = transitions.remove();

                if (transition.tokens.isEmpty()
                        || tokenStack.size() > 0 && transition.tokens.contains(tokenStack.peek())) {
                    if (transition.symbol.isEmpty()) {
                        execute(transition.actions);

                        transitions.add(transition);
                    }
                    else if(transition.symbol.isWild()) {
                        wildTrans.add(transition);
                    }
                    else if (transition.symbol.isChar()) {
                        if (transition.symbol.getChar() == symbol) {
                            execute(transition.actions);

                            targets.add(transition.target);
                        }
                    }
                    else if (transition.symbol.isRange()) {
                        if (symbol >= transition.symbol.getBegin() && symbol <= transition.symbol.getEnd()) {
                            execute(transition.actions);

                            targets.add(transition.target);
                        }
                    }
                    else {
                        throw new RuntimeException();
                    }
                }
            }

            // If no target was found, consider the wild transitions
            if (wildTrans.size() > 0 && targetCount0 == targets.size()) {
                for (var transition : wildTrans) {
                    execute(transition.actions);

                    targets.add(transition.target);
                }
            }

            wildTrans.clear();
        }

        return targets;
    }

    private void execute(List<Action> actions) {
        for (var action : actions) {
            if (action instanceof TokenPushAction) {
                tokenStack.push(((TokenPushAction) action).token);
            }
            else if (action instanceof TokenPopAction) {
                tokenStack.pop();
            }
            else {
                // TODO
            }
        }
    }

}
