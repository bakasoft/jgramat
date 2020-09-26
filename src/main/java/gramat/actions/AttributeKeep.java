package gramat.actions;

import gramat.eval.Context;

import java.io.PrintStream;
import java.util.List;

public class AttributeKeep extends Action {

    public AttributeKeep(int order) {
        super(order);
    }

    @Override
    public boolean stacks(Action other) {
        return other instanceof AttributeKeep;
    }

    @Override
    public void printAmCode(PrintStream out) {
        out.print("ATTRIBUTE BEGIN");
    }

    @Override
    public void run(Context context) {
        context.pushContainer();
    }

    @Override
    public List<String> getArguments() {
        return List.of();
    }
}
