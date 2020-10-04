package gramat.actions;

import gramat.eval.Context;
import gramat.eval.RejectedException;

import java.util.List;
import java.util.Objects;

import static gramat.util.Validations.tryCastAndTest;

public class AttributeEnd extends Action {

    private final int trxID;
    private final String defaultName;

    public AttributeEnd(int trxID, String defaultName) {
        this.trxID = trxID;
        this.defaultName = defaultName;
    }

    @Override
    public boolean contains(Action other) {
        return tryCastAndTest(
                AttributeEnd.class,
                other,
                a -> a.trxID == this.trxID && Objects.equals(this.defaultName, a.defaultName)
        );
    }

    @Override
    public void run(Context context) {
        var name = context.getName();
        var value = context.popValue();

        if (name == null) {
            if (defaultName == null) {
                throw new RejectedException("missing name");
            }
            else {
                name = defaultName;
            }
        }
        else if (defaultName != null) {
            throw new RejectedException("conflicted name");
        }

        context.setAttribute(name, value);
    }

    @Override
    public List<String> getArguments() {
        if (defaultName != null) {
            return List.of(String.valueOf(trxID), defaultName);
        }
        return List.of(String.valueOf(trxID));
    }
}
