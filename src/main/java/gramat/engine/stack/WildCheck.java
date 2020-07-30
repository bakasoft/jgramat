package gramat.engine.stack;

public class WildCheck extends ControlCheck {

    WildCheck() {}

    @Override
    public boolean test(ControlStack stack) {
        return false;
    }

    @Override
    public void apply(ControlStack stack) {

    }

    @Override
    public boolean compare(ControlCheck check) {
        return check instanceof WildCheck;
    }

    @Override
    public String toString() {
        return "WILD";
    }

}
