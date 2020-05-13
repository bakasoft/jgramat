package gramat.automata.ndfa;

import gramat.output.GrammarWriter;

public class DTransitionRange extends DTransition {

    final char begin;
    final char end;

    public DTransitionRange(DState target, char begin, char end) {
        super(target);
        this.begin = begin;
        this.end = end;
    }

    @Override
    public boolean accepts(char symbol) {
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
