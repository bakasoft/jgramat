package gramat.automata.ndfa;

import gramat.eval.Action;
import gramat.output.GrammarWriter;

public class DTransitionWild extends DTransition {

    public DTransitionWild(DState target, Action[] actions) {
        super(target, actions);
    }

    @Override
    public boolean accepts(int symbol) {
        throw new RuntimeException();
    }

    @Override
    public boolean intersects(DTransition transition) {
        throw new RuntimeException();
    }

    @Override
    public void write(GrammarWriter writer) {
        throw new RuntimeException();
    }

}
