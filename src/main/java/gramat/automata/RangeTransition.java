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
    public boolean contains(Transition tr) {
        if (tr instanceof CharTransition) {
            var ct = (CharTransition) tr;
            return ct.value >= this.begin && ct.value <= this.end;
        }
        else if (tr instanceof RangeTransition) {
            var rt = (RangeTransition) tr;
            return rt.begin >= this.begin && rt.end <= this.end;
        }
        else {
            throw new RuntimeException();
        }
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
