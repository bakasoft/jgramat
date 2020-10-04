package gramat.actions;

import gramat.eval.Context;

import java.util.List;

import static gramat.util.Validations.tryCastAndTest;

public class NameEnd extends Action {

    private final int trxID;

    public NameEnd(int trxID) {
        this.trxID = trxID;
    }

    @Override
    public boolean contains(Action other) {
        return tryCastAndTest(
                NameEnd.class,
                other,
                a -> a.trxID == this.trxID
        );
    }

    @Override
    public void run(Context context) {
        var name = context.popValue();

        context.setName(String.valueOf(name));
    }

    @Override
    public List<String> getArguments() {
        return List.of(String.valueOf(trxID));
    }
}
