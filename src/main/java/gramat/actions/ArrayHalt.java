package gramat.actions;

import gramat.eval.Context;

import java.io.PrintStream;
import java.util.List;

public class ArrayHalt extends Action {

    private final Object typeHint;

    public ArrayHalt(int order, Object typeHint) {
        super(order);
        this.typeHint = typeHint;
    }

    @Override
    public boolean stacks(Action other) {
        return false;
    }

    @Override
    public void printAmCode(PrintStream out) {
        out.print("ARRAY END");
    }

    @Override
    public void run(Context context) {
        var container = context.popContainer();
        var array = container.popArray();

        context.pushValue(array);
    }

    @Override
    public List<String> getArguments() {
        return List.of();
    }
}
