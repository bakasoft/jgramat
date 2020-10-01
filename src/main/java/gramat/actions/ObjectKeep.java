package gramat.actions;

import gramat.eval.Context;

import java.io.PrintStream;
import java.util.List;

public class ObjectKeep extends Action {

    public final int trxID;

    public ObjectKeep(int trxID) {
        this.trxID = trxID;
    }

    @Override
    public boolean stacks(Action other) {
        return other instanceof ObjectKeep;
    }

    @Override
    public void printAmCode(PrintStream out) {
        out.print("OBJECT BEGIN");
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
