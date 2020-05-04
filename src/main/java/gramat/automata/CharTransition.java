package gramat.automata;

import gramat.output.GrammarWriter;

public class CharTransition extends Transition {

    public final char value;

    public CharTransition(State state, char value) {
        super(state);
        this.value = value;
    }

    @Override
    public boolean test(char c) {
        return this.value == c;
    }

    @Override
    public boolean contains(Transition tr) {
        if (tr instanceof CharTransition) {
            var ct = (CharTransition) tr;
            return ct.value == this.value;
        }
        else if (tr instanceof RangeTransition) {
            var rt = (RangeTransition) tr;
            return rt.begin == this.value && rt.end == this.value;
        }
        else {
            throw new RuntimeException();
        }
    }

    @Override
    public void write(GrammarWriter writer) {
        if (writer.open(this, "char-transition")) {
            writer.attribute("value", value);
            state.write(writer);
            writer.close();
        }
    }
}
