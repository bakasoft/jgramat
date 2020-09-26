package gramat.actions;

import gramat.eval.Context;
import gramat.parsers.ValueParser;

import java.io.PrintStream;
import java.util.List;

public class ValueHalt extends Action {

    private final ValueParser parser;

    public ValueHalt(int order, ValueParser parser) {
        super(order);
        this.parser = parser;
    }

    @Override
    public boolean stacks(Action other) {
        if (other instanceof ValueHalt) {
            var otherEnd = (ValueHalt)other;

            return otherEnd.parser == parser;
        }
        return false;
    }

    @Override
    public void printAmCode(PrintStream out) {
        out.print("VALUE END");
    }

    @Override
    public void run(Context context) {
        context.transactions.commit(trxID);
    }

    @Override
    public List<String> getArguments() {
        if (parser == null || parser.getName() == null) {// TODO
            return List.of();
        }
        return List.of(parser.getName());
    }
}
