package gramat.engine.control;

abstract public class Check {

    abstract public boolean test(ControlStack stack);

    abstract public void apply(ControlStack stack);

    abstract public boolean compare(Check check);

    public boolean isNull() {
        return this instanceof CheckNull;
    }

}
