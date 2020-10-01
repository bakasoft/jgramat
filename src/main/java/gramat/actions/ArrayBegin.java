package gramat.actions;

import gramat.eval.Context;

import java.util.List;

import static gramat.util.Validations.tryCastAndTest;

public class ArrayBegin extends Action {

    public final int trxID;

    public ArrayBegin(int trxID) {
        this.trxID = trxID;
    }

    @Override
    public boolean contains(Action other) {
        return tryCastAndTest(
                ArrayBegin.class,
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
            context.pushContainer();
            context.transaction().begin(id, () -> {
                var container = context.popContainer();
                var array = container.popArray();

                context.pushValue(array);
            });
        }
    }

    @Override
    public List<String> getArguments() {
        return List.of(String.valueOf(trxID));
    }
}
