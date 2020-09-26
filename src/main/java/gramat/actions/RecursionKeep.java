package gramat.actions;

import gramat.eval.Context;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;

public class RecursionKeep extends Action {

    public final String token;

    public RecursionKeep(int order, String token) {
        super(order);
        this.token = token;
    }

    @Override
    public boolean stacks(Action other) {
        if (other instanceof RecursionKeep) {
            var o = (RecursionKeep)other;

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
