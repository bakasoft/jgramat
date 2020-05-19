package gramat.automata.dfa;

import gramat.output.GrammarWriter;

public class DTransitionWild extends DTransition {

    public DTransitionWild(DState target) {
        super(target);
    }

    @Override
    public boolean accepts(int symbol) {
        throw new RuntimeException();
    }

    @Override
    public boolean intersects(DTransition transition) {
        return false;
    }

    @Override
    public void write(GrammarWriter writer) {
        throw new RuntimeException();
    }

}
