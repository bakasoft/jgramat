package gramat.engine.stack;

abstract public class ControlCheck {

    abstract public boolean test(ControlStack stack);

    abstract public void apply(ControlStack stack);

    abstract public boolean compare(ControlCheck check);

}
