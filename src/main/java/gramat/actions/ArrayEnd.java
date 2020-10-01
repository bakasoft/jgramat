package gramat.actions;

import gramat.eval.Context;

import java.util.List;
import java.util.Objects;

import static gramat.util.Validations.tryCastAndTest;

public class ArrayEnd extends Action {

    private final int trxID;
    private final Object typeHint;

    public ArrayEnd(int trxID, Object typeHint) {
        this.trxID = trxID;
        this.typeHint = typeHint;
    }

    @Override
    public boolean contains(Action other) {
        return tryCastAndTest(
                ArrayEnd.class,
                other,
                a -> a.trxID == this.trxID && Objects.equals(this.typeHint, a.typeHint)
        );
    }

    @Override
    public void run(Context context) {
        context.transaction().commit(context.transactionID(trxID));
    }

    @Override
    public List<String> getArguments() {
        return List.of(String.valueOf(trxID));
    }
}
