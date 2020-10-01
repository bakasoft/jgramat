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
        var id = context.transactionID(trxID);

        if (context.transaction().contains(id)) {
            context.transaction().keep(id);
        }
        else {
            context.pushContainer();
            context.transaction().begin(id, () -> {
                var container = context.popContainer();
                var attributes = container.getAttributes();

                context.pushValue(new MapData(null, attributes)); // TODO typehint
            });
        }
    }

    @Override
    public List<String> getArguments() {
        return List.of(String.valueOf(trxID));
    }

}
