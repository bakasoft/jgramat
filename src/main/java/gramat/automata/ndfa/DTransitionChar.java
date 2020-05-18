package gramat.automata.ndfa;

import gramat.eval.Action;
import gramat.output.GrammarWriter;

public class DTransitionChar extends DTransition {

    public final int symbol;

    public DTransitionChar(DState target, Action[] actions, int symbol) {
        super(target, actions);
        this.symbol = symbol;
    }

    @Override
    public boolean accepts(int symbol) {
        return this.symbol == symbol;
    }

    @Override
    public boolean intersects(DTransition transition) {
        if (transition instanceof DTransitionChar) {
            var tc = (DTransitionChar)transition;

            return tc.symbol == this.symbol;
        }
        else if (transition instanceof DTransitionRange) {
            var tr = (DTransitionRange)transition;

            return this.symbol >= tr.begin && this.symbol <= tr.end;
        }
        else {
            throw new RuntimeException("Unsupported transition: " + transition);
        }
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "char-transition")) {
            writer.attribute("value", symbol);
            target.write(writer);
            writer.close();
        }
    }

}
