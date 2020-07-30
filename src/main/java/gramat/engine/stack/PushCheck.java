package gramat.engine.stack;

import java.util.Objects;

public class PushCheck extends ControlCheck {

    public final String token;

    PushCheck(String token) {
        this.token = token;
    }

    @Override
    public boolean test(ControlStack stack) {
        return true;
    }

    @Override
    public void apply(ControlStack stack) {
        stack.push(token);
    }

    @Override
    public boolean compare(ControlCheck check) {
        if (check instanceof PushCheck) {
            return Objects.equals(this.token, ((PushCheck) check).token);
        }
        return false;
    }

    @Override
    public String toString() {
        return "PUSH[" + token + "]";
    }

}
