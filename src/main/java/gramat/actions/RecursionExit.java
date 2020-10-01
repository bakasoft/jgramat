package gramat.actions;

import gramat.eval.Context;

import java.util.List;
import java.util.Objects;

import static gramat.util.Validations.tryCastAndTest;

public class RecursionExit extends Action {

    public final String token;

    public RecursionExit(String token) {
        this.token = token;
    }

    @Override
    public boolean contains(Action other) {
        return tryCastAndTest(
                RecursionExit.class,
                other,
                a -> Objects.equals(this.token, a.token)
        );
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
