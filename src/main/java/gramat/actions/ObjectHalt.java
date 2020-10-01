package gramat.actions;

import gramat.data.MapData;
import gramat.eval.Context;

import java.io.PrintStream;
import java.util.List;

public class ObjectHalt extends Action {

    private final int trxID;
    private final Object typeHint;

    public ObjectHalt(int trxID, Object typeHint) {
        this.trxID = trxID;
        this.typeHint = typeHint;
    }

    @Override
    public boolean stacks(Action other) {
        return false;
    }

    @Override
    public void printAmCode(PrintStream out) {
        out.print("OBJECT END");
    }

    @Override
    public void run(Context context) {
        var container = context.popContainer();
        var attributes = container.getAttributes();

        context.pushValue(new MapData(null, attributes)); // TODO typehint
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
