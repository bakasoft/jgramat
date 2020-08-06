package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class JoinCancel extends ValueAction {

    private final JoinPress press;

    public JoinCancel(JoinPress press) {
        this.press = press;
    }

    @Override
    public void run(ValueRuntime runtime) {
        // TODO implement
    }

    @Override
    public String getDescription() {
        return "CANCEL JOIN";
    }
}
