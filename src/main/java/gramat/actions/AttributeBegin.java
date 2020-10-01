package gramat.actions;

import gramat.eval.Context;

import java.util.List;
import java.util.Objects;

import static gramat.util.Validations.tryCastAndTest;

public class AttributeBegin extends Action {

    public final int trxID;
    private final String defaultName;

    public AttributeBegin(int trxID, String defaultName) {
        this.trxID = trxID;
        this.defaultName = defaultName;
    }

    @Override
    public boolean contains(Action other) {
        return tryCastAndTest(
                AttributeBegin.class,
                other,
                a -> a.trxID == this.trxID && Objects.equals(this.defaultName, a.defaultName)
        );
    }


    @Override
    public void run(Context context) {
        var id = context.transactionID(trxID);

        if (context.transaction().contains(id)) {
            context.transaction().keep(id);
        }
        else {
            context.pushContainer();
            context.transaction().begin(id, () -> {
                var container = context.popContainer();

                var name = container.popName(defaultName);

                var value = container.popValue();

                container.expectEmptyValues();

                context.peekContainer().setAttribute(name, value);
            });
        }
    }

    @Override
    public List<String> getArguments() {
        return List.of(String.valueOf(trxID));
    }
}
