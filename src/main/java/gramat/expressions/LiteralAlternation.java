package gramat.expressions;

import gramat.compiling.Compiler;
import gramat.runtime.EvalContext;
import gramat.util.parsing.Location;

import java.util.ArrayList;
import java.util.List;

public class LiteralAlternation extends Expression {

    private static class CharState {

        public final char value;

        public final List<CharState> nexts;

        public boolean valid;

        public CharState(char value, boolean valid) {
            this.value = value;
            this.valid = valid;
            this.nexts = new ArrayList<>();
        }
    }

    private final List<CharState> roots;

    public LiteralAlternation(Location location, String[] values) {
        super(location);
        roots = build_states(values);
    }

    @Override
    public boolean eval(EvalContext context) {
        var pos0 = context.source.getPosition();
        var currentStates = roots;
        var state = (CharState)null;

        while(true) {
            Character c = context.source.peek();

            if (c == null) {
                break;
            }

            var currentState = (CharState) null;

            // find the correct state
            for (var s : currentStates) {
                if (c == s.value) {
                    currentState = s;
                    break;
                }
            }

            if(currentState == null) {
                break;
            }

            state = currentState;

            context.source.moveNext();

            currentStates = state.nexts;

            if (currentStates.isEmpty()) {
                break;
            }
        }

        if (state != null && state.valid) {
            return true;
        }

        context.source.setPosition(pos0);
        return false;
    }

    @Override
    public String getDescription() {
        return "Literal-Alternation";
    }

    @Override
    public Expression _custom_optimize(Compiler context) {
        return this;
    }

    @Override
    public List<Expression> getInnerExpressions() {
        return listOf();
    }

    private static List<CharState> build_states(String[] values) {
        List<CharState> states = new ArrayList<>();
        var roots = states;

        for (var value : values) {
            for (int i = 0; i < value.length(); i++) {
                var c = value.charAt(i);
                var valid = (i == value.length() - 1);
                var state = get_or_create_state(c, valid, states);

                states = state.nexts;
            }

            states = roots;
        }

        return roots;
    }

    private static CharState get_or_create_state(char c, boolean valid, List<CharState> states) {
        for (var state : states) {
            if (state.value == c) {
                state.valid = valid;
                return state;
            }
        }

        var state = new CharState(c, valid);

        states.add(state);

        return state;
    }

}
