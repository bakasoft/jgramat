package gramat.engine.checks;

import java.util.Objects;

public class CheckPop extends Check {

    public final String token;

    CheckPop(String token) {
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
    public boolean compare(Check check) {
        if (check instanceof CheckPop) {
            return Objects.equals(this.token, ((CheckPop) check).token);
        }
        return false;
    }

    @Override
    public String toString() {
        return "POP[" + token + "]";
    }
}
