package gramat.automata;

import gramat.output.GrammarWriter;

public class CharTransition extends Transition {

    public final boolean complement;
    public final char value;

    public CharTransition(State state, char value, boolean complement) {
        super(state);
        this.complement = complement;
        this.value = value;
    }

    @Override
    public boolean test(char c) {
        return complement == (this.value != c);
    }

    @Override
    public boolean isWild() {
        return false;
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
