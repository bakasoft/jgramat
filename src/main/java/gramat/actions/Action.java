package gramat.actions;

import gramat.eval.Context;

import java.io.PrintStream;
import java.util.List;

abstract public class Action {

    public final int trxID;

    public Action(int trxID) {
        this.trxID = trxID;
    }

    abstract public boolean stacks(Action other);

    abstract public void printAmCode(PrintStream out);

    abstract public void run(Context context);

    abstract public List<String> getArguments();

    public final String getKey() {
        for (var item : ActionKeys.values()) {
            if (item.type.isInstance(this)) {
                return item.key;
            }
        }

        throw new RuntimeException();
    }

    @Override
    public final String toString() {
        return getKey() + "(" + String.join(", ", getArguments()) + ")" + trxID;
    }

}
