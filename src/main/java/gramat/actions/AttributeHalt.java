package gramat.actions;

import gramat.eval.Context;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;

public class AttributeHalt extends Action {

    private final int trxID;
    private final String defaultName;

    public AttributeHalt(int trxID, String defaultName) {
        this.trxID = trxID;
        this.defaultName = defaultName;
    }

    @Override
    public boolean stacks(Action other) {
        if (other instanceof AttributeHalt) {
            var o = (AttributeHalt)other;

            return Objects.equals(this.defaultName, o.defaultName);
        }
        return false;
    }

    @Override
    public void printAmCode(PrintStream out) {
        out.print("ATTRIBUTE END");
    }

    @Override
    public void run(Context context) {
        var container = context.popContainer();

        var name = container.popName(defaultName);

        var value = container.popValue();

        container.expectEmptyValues();

        context.peekContainer().setAttribute(name, value);
    }

    @Override
    public List<String> getArguments() {
        if (defaultName != null) {
            return List.of(String.valueOf(trxID), defaultName);
        }
        return List.of(String.valueOf(trxID));
    }
}
