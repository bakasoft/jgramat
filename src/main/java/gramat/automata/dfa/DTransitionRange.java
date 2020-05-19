package gramat.automata.dfa;

import gramat.output.GrammarWriter;

public class DTransitionRange extends DTransition {

    public final int begin;
    public final int end;

    public DTransitionRange(DState target, int begin, int end) {
        super(target);
        this.begin = begin;
        this.end = end;
    }

    @Override
    public boolean accepts(int symbol) {
        return symbol >= begin && symbol <= end;
    }

    @Override
    public boolean intersects(DTransition transition) {
        if (transition instanceof DTransitionChar) {
            var tc = (DTransitionChar)transition;

            return tc.symbol >= begin && tc.symbol <= end;
        }
        else if (transition instanceof DTransitionRange) {
            var tr = (DTransitionRange)transition;

            return this.begin >= tr.begin && this.begin <= tr.end
                    || this.end >= tr.begin && this.end <= tr.end;
        }
        else if (transition instanceof DTransitionWild) {
            return false;
        }
        else {
            throw new RuntimeException("Unsupported transition: " + transition);
        }
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "range-transition")) {
            writer.attribute("begin", begin);
            writer.attribute("end", end);
            target.write(writer);
            writer.close();
        }
    }
}
