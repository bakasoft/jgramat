package gramat.actions;

import gramat.eval.Context;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;

public class RecursionEnter extends Action {

    public final String token;

    public RecursionEnter(String token) {
        this.token = token;
    }

    @Override
    public boolean stacks(Action other) {
        if (other instanceof RecursionEnter) {
            var o = (RecursionEnter)other;

            return Objects.equals(token, o.token);
        }
        return false;
    }

    @Override
    public void printAmCode(PrintStream out) {
        out.print("ENTER " + token);
    }

    @Override
    public void run(Context context) {
        context.pushCall(token);
    }

    @Override
    public List<String> getArguments() {
        return List.of(token);
    }
}
