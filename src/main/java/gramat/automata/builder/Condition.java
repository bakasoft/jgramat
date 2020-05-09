package gramat.automata.builder;

abstract public class Condition {

    abstract public boolean matches(Condition condition);

    abstract public boolean contains(Condition condition);

    // TODO add intersects method

}
