package gramat.automata;

import gramat.output.GrammarWriter;

public class RangeTransition extends Transition {

    public final char begin;
    public final char end;

    public RangeTransition(State state, char begin, char end) {
        super(state);
        this.begin = begin;
        this.end = end;
    }

    @Override
    public boolean test(char c) {
        return c >= begin && c <= end;
    }

    @Override
    public boolean isWild() {
        return false;
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "range-transition")) {
            writer.attribute("begin", begin);
            writer.attribute("end", end);
            state.write(writer);
            writer.close();
        }
    }

}
