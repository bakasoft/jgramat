package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class ListCancel extends ValueAction {

    private final ListPress press;

    public ListCancel(ListPress press) {
        this.press = press;
    }

    @Override
    public void run(ValueRuntime runtime) {
        // TODO implement
    }

    @Override
    public String getDescription() {
        return "CANCEL LIST";
    }
}
