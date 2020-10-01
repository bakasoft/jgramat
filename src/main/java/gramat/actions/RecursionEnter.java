package gramat.actions;

import gramat.eval.Context;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;

import static gramat.util.Validations.tryCastAndTest;

public class RecursionEnter extends Action {

    public final String token;

    public RecursionEnter(String token) {
        this.token = token;
    }

    @Override
    public boolean contains(Action other) {
        return tryCastAndTest(
                RecursionEnter.class,
                other,
                a -> Objects.equals(this.token, a.token)
        );
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
