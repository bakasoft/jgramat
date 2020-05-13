package gramat.automata.ndfa;

import gramat.output.GrammarWriter;

abstract public class DTransition {

    abstract public boolean accepts(char symbol);

    abstract public boolean intersects(DTransition transition);

    final DState target;

    public DTransition(DState target) {
        this.target = target;
    }

    public abstract void write(GrammarWriter writer);
}
