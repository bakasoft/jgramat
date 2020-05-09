package gramat.automata.builder;

import gramat.util.GramatWriter;

public class ConditionRange extends Condition {

    public final char begin;
    public final char end;

    public ConditionRange(char begin, char end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public boolean matches(Condition condition) {
        if (condition instanceof ConditionChar) {
            var cc = (ConditionChar) condition;

            return cc.value == this.begin && cc.value == this.end;
        }
        else if (condition instanceof ConditionRange) {
            var cr = (ConditionRange) condition;

            return cr.begin == this.begin && cr.end == this.end;
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

            return cc.value >= this.begin && cc.value <= this.end;
        }
        else if (condition instanceof ConditionRange) {
            var cr = (ConditionRange) condition;

            return cr.begin >= this.begin && cr.end <= this.end;
        }
        else {
            throw new RuntimeException();
        }
    }

    @Override
    public String toString() {
        var beginStr = GramatWriter.toDelimitedString(String.valueOf(begin), '`');
        var endStr = GramatWriter.toDelimitedString(String.valueOf(end), '`');
        return "[" + beginStr + "," + endStr + "]";
    }
}
