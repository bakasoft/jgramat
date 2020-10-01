package gramat.actions;

import gramat.eval.Context;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;

public class RecursionExit extends Action {

    public final String token;

    public RecursionExit(String token) {
        this.token = token;
    }

    @Override
    public boolean stacks(Action other) {
        if (other instanceof RecursionExit) {
            var o = (RecursionExit)other;

            return Objects.equals(token, o.token);
        }
        return false;
    }

    @Override
    public void printAmCode(PrintStream out) {
        out.print("EXIT " + token);
    }

    @Override
    public void run(Context context) {
        context.popCall(token);
    }

    @Override
    public List<String> getArguments() {
        return List.of(token);
    }
}
