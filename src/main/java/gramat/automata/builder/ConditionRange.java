package gramat.automata.builder;

public class ConditionRange extends Condition {

    public final char begin;
    public final char end;

    public ConditionRange(char begin, char end) {
        this.begin = begin;
        this.end = end;
    }

}
