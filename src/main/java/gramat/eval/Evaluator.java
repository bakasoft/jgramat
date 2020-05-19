package gramat.eval;

import gramat.automata.dfa.DState;
import gramat.runtime.*;
import gramat.util.parsing.Source;

import java.util.ArrayList;
import java.util.HashMap;

public class Evaluator {

    private final Source source;

    private final HashMap<Action, Integer> actionsPositions = new HashMap<>();

    public Evaluator(EvalContext context) {
        this.source = context.source;
    }

    public boolean eval(DState root) {
        var pos0 = source.getPosition();
        var state = root;
        int lastValue = 0;

        System.out.println("---------->>");
        System.out.println(state.getAmCode());
        System.out.println("----------");

        while(true) {
            try {
                if (state.isFinal()) {
                    if (state.isAccepted()) {
                        return true;
                    } else {
                        source.setPosition(pos0);
                        return false;
                    }
                } else {
                    var value = source.peek();

                    DState newState;

                    if (lastValue == Source.EOF && value == lastValue) {
                        newState = null;
                    }
                    else {
                        var actions = new ArrayList<Action>();
                        newState = state.move(value, actions);
                        for (var action : actions) {
                            action.run(this);
                        }
                    }

                    if (newState == null) {
                        if (state.isAccepted()) {
                            return true;
                        } else {
                            source.setPosition(pos0);
                            return false;
                        }
                    }

                    // check for infinite loop
                    source.moveNext();

                    state = newState;
                    lastValue = value;
                }
            }
            catch(Reject e) {
                source.setPosition(pos0);
                return false;
            }
        }
    }

    private static class Reject extends RuntimeException {}

}
