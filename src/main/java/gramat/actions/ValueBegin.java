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
        var id = context.transactionID(trxID);
        if (context.transaction().contains(id)) {
            context.transaction().keep(id);
        }
        else {
            var begin = context.tape.getPosition();

            context.transaction().begin(id, () -> {
                var end = context.tape.getPosition();
                var text = context.tape.extract(begin, end);
                context.peekContainer().pushValue(text); // TODO parser?
            });
        }
    }

    @Override
    public List<String> getArguments() {
        return List.of(String.valueOf(trxID));
    }
}
