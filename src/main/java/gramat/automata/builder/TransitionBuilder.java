package gramat.automata.builder;

public class TransitionBuilder {

    public final StateBuilder source;
    public final Condition condition;
    public final StateBuilder target;

    public TransitionBuilder(StateBuilder source, Condition condition, StateBuilder target) {
        this.source = source;
        this.condition = condition;
        this.target = target;
    }
}
