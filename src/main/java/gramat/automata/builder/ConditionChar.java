package gramat.automata.builder;

import gramat.util.CodeWriter;
import gramat.util.GramatWriter;

public class ConditionChar extends Condition {

    public final char value;

    public ConditionChar(char value) {
        this.value = value;
    }

    @Override
    public boolean matches(Condition condition) {
        if (condition instanceof ConditionChar) {
            var cc = (ConditionChar) condition;

            return cc.value == this.value;
        }
        else if (condition instanceof ConditionRange) {
            var cr = (ConditionRange) condition;

            return cr.begin == this.value && cr.end == this.value;
        }
        else if (condition instanceof ConditionElse) {
            return false;
        }
        else {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean contains(Condition condition) {
        if (condition instanceof ConditionChar) {
            var cc = (ConditionChar) condition;

            return cc.value == this.value;
        }
        else if (condition instanceof ConditionRange) {
            var cr = (ConditionRange) condition;

            return cr.begin == this.value && cr.end == this.value;
        }
        else {
            throw new RuntimeException();
        }
    }

    @Override
    public String toString() {
        return "[" + GramatWriter.toDelimitedString(String.valueOf(value), '`') + "]";
    }
}
