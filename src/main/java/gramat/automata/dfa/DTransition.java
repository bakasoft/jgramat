package gramat.automata.dfa;

import gramat.eval.Action;
import gramat.output.GrammarWriter;

import java.util.ArrayList;
import java.util.List;

abstract public class DTransition {

    abstract public boolean accepts(int symbol);

    abstract public boolean intersects(DTransition transition);

    public final DState target;

    public final List<Action> actions;

    public DTransition(DState target) {
        this.target = target;
        this.actions = new ArrayList<>();
    }

    public abstract void write(GrammarWriter writer);

    abstract public String getSymbol();
}
