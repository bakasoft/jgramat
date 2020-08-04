package gramat.engine.checks;

public class CheckAny extends Check {

    CheckAny() {}

    @Override
    public boolean test(ControlStack stack) {
        return true;
    }

    @Override
    public void apply(ControlStack stack) {
        // do nothing
    }

    @Override
    public boolean compare(Check check) {
        return check instanceof CheckAny;
    }

    @Override
    public String toString() {
        return "ANY";
    }

}
