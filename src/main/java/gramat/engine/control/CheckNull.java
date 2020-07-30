package gramat.engine.control;

public class CheckNull extends Check {

    CheckNull() {}

    @Override
    public boolean test(ControlStack stack) {
        return false;
    }

    @Override
    public void apply(ControlStack stack) {

    }

    @Override
    public boolean compare(Check check) {
        return check instanceof CheckNull;
    }

    @Override
    public String toString() {
        return "¶";
    }

}
