package gramat.engine.stack;

import java.util.Objects;

public class PopCheck extends ControlCheck {

    public final String token;

    PopCheck(String token) {
        this.token = token;
    }

    @Override
    public boolean test(ControlStack stack) {
        return stack.test(token);
    }

    @Override
    public void apply(ControlStack stack) {
        stack.pop();
    }

    @Override
    public boolean compare(ControlCheck check) {
        if (check instanceof PopCheck) {
            return Objects.equals(this.token, ((PopCheck) check).token);
        }
        return false;
    }

    @Override
    public String toString() {
        return "POP[" + token + "]";
    }
}
