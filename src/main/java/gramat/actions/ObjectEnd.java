package gramat.actions;

import gramat.eval.Context;

import java.util.List;
import java.util.Objects;

import static gramat.util.Validations.tryCastAndTest;

public class ObjectEnd extends Action {

    private final int trxID;
    private final Object typeHint;

    public ObjectEnd(int trxID, Object typeHint) {
        this.trxID = trxID;
        this.typeHint = typeHint;
    }

    @Override
    public boolean contains(Action other) {
        return tryCastAndTest(
                ObjectEnd.class,
                other,
                a -> a.trxID == this.trxID && Objects.equals(this.typeHint, a.typeHint)
        );
    }

    @Override
    public void run(Context context) {
        var id = context.transactionID(trxID);

        context.transaction().commit(id);
    }

    @Override
    public List<String> getArguments() {
        if (typeHint != null) {
//            return List.of(typeHint);
            // TODO serialize typehint
        }
        return List.of(String.valueOf(trxID));
    }
}
