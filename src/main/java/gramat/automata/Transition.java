package gramat.automata;

import gramat.output.GrammarWriter;

abstract public class Transition {

    public State state;

    public Transition(State state) {
        this.state = state;
    }

    abstract public boolean test(char c);

    public abstract void write(GrammarWriter writer);

    public abstract boolean isWild();

}
