package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class JoinRelease extends ValueAction {

    private final JoinPress press;

    public JoinRelease(JoinPress press) {
        this.press = press;
    }

    @Override
    public void run(ValueRuntime runtime) {
        // TODO implement
    }

    @Override
    public String getDescription() {
        return "RELEASE JOIN";
    }
}
