package gramat.automata;

import gramat.output.GrammarWriter;

public class WildTransition extends Transition {
    public WildTransition(State target) {
        super(target);
    }

    @Override
    public boolean test(char c) {
        return false;
    }

    @Override
    public void write(GrammarWriter writer) {

    }

    @Override
    public boolean isWild() {
        return true;
    }
}
