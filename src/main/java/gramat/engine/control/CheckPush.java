package gramat.engine.control;

import java.util.Objects;

public class CheckPush extends Check {

    public final String token;

    CheckPush(String token) {
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
    public boolean compare(Check check) {
        if (check instanceof CheckPush) {
            return Objects.equals(this.token, ((CheckPush) check).token);
        }
        return false;
    }

    @Override
    public String toString() {
        return "PUSH[" + token + "]";
    }

}
