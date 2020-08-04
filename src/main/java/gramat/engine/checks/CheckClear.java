package gramat.engine.checks;

public class CheckClear extends Check {

    CheckClear() {}

    @Override
    public boolean test(ControlStack stack) {
        return stack.isClear();
    }

    @Override
    public void apply(ControlStack stack) {
        // do nothing
    }

    @Override
    public boolean compare(Check check) {
        return check instanceof CheckClear;
    }

    @Override
    public String toString() {
        return "CLEAR";
    }

}
