package gramat.actions;

import gramat.data.MapData;
import gramat.eval.Context;

import java.util.List;

import static gramat.util.Validations.tryCastAndTest;

public class ObjectBegin extends Action {

    public final int trxID;

    public ObjectBegin(int trxID) {
        this.trxID = trxID;
    }

    @Override
    public boolean contains(Action other) {
        return tryCastAndTest(
                ObjectBegin.class,
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
