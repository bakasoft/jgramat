package gramat.actions;

import gramat.eval.Context;

import java.util.List;

import static gramat.util.Validations.tryCastAndTest;

public class ValueBegin extends Action {

    public final int trxID;

    public ValueBegin(int trxID) {
        this.trxID = trxID;
    }

    @Override
    public boolean contains(Action other) {
        return tryCastAndTest(
                ValueBegin.class,
                other,
                a -> a.trxID == this.trxID
        );
    }

    @Override
    public void run(Context context) {
        var begin = context.tape.getPosition();

        context.pushPosition(begin);
    }

    @Override
    public List<String> getArguments() {
        return List.of(String.valueOf(trxID));
    }
}
