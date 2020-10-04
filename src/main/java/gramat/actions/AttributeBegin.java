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
        context.pushContainer();
    }

    @Override
    public List<String> getArguments() {
        return List.of(String.valueOf(trxID));
    }
}
