package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class ListRelease extends ValueAction {

    private final ListPress press;

    public ListRelease(ListPress press) {
        this.press = press;
    }

    @Override
    public void run(ValueRuntime runtime) {
        // TODO implement
    }

    @Override
    public String getDescription() {
        return "RELEASE LIST";
    }
}
