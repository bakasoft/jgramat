package gramat.engine.stack;

public class EmptyCheck extends ControlCheck {

    EmptyCheck() {}

    @Override
    public boolean test(ControlStack stack) {
        return stack.empty();
    }

    @Override
    public void apply(ControlStack stack) {
        // do nothing
    }

    @Override
    public boolean compare(ControlCheck check) {
        return check instanceof EmptyCheck;
    }

    @Override
    public String toString() {
        return "EMPTY";
    }

}
