package gramat.actions;

import gramat.eval.Context;

import java.io.PrintStream;
import java.util.List;

public class NameKeep extends Action {

    public final int trxID;

    public NameKeep(int trxID) {
        this.trxID = trxID;
    }

    @Override
    public boolean stacks(Action other) {
        return other instanceof NameKeep;
    }

    @Override
    public void printAmCode(PrintStream out) {
        out.print("NAME BEGIN");
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
