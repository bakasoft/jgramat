package gramat.actions;

import gramat.eval.Context;

import java.util.List;

import static gramat.util.Validations.tryCastAndTest;

public class NameBegin extends Action {

    public final int trxID;

    public NameBegin(int trxID) {
        this.trxID = trxID;
    }

    @Override
    public boolean contains(Action other) {
        return tryCastAndTest(
                NameBegin.class,
                other,
                a -> a.trxID == this.trxID
        );
    }

    @Override
    public void run(Context context) {
        context.pushContainer();
    }

    @Override
    public List<String> getArguments() {
        return List.of(String.valueOf(trxID));
    }
}
