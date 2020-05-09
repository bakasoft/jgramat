package gramat.automata.builder;

public class ConditionElse extends Condition {

    @Override
    public boolean matches(Condition condition) {
        return condition instanceof ConditionElse;
    }

    @Override
    public boolean contains(Condition condition) {
        return false;
    }

    @Override
    public String toString() {
        return "[*]";
    }
}
