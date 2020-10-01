package gramat.actions;

import gramat.eval.Context;

import java.io.PrintStream;
import java.util.List;

public class ValueKeep extends Action {

    public final int trxID;

    public ValueKeep(int trxID) {
        this.trxID = trxID;
    }

    @Override
    public boolean stacks(Action other) {
        // TODO
        return other instanceof ValueKeep;
    }

    @Override
    public void printAmCode(PrintStream out) {
        out.print("VALUE BEGIN");
    }

    @Override
    public void run(Context context) {
        if (context.transactions.contains(trxID)) {
            context.transactions.keep(trxID);
        }
        else {
            var begin = context.tape.getPosition();

            context.transactions.begin(trxID, () -> {
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
