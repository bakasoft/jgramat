package gramat.automata.ndfa;

import gramat.eval.Action;
import gramat.output.GrammarWriter;

abstract public class DTransition {

    abstract public boolean accepts(int symbol);

    abstract public boolean intersects(DTransition transition);

    public final DState target;

    public final Action[] actions;

    public DTransition(DState target, Action[] actions) {
        this.target = target;
        this.actions = actions;
    }

    public abstract void write(GrammarWriter writer);
}
