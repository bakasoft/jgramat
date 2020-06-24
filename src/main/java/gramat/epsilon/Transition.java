package gramat.epsilon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Transition {

    public final Symbol symbol;
    public final Set<Token> tokens;
    public final List<Action> actions;
    public final State source;
    public final State target;

    public Transition(State source, State target, Symbol symbol) {
        this.symbol = symbol;
        this.source = source;
        this.target = target;
        this.tokens = new HashSet<>();
        this.actions = new ArrayList<>();
    }

}
