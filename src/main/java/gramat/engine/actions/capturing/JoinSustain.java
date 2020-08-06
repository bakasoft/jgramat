package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class JoinSustain extends ValueAction {

    private final JoinPress press;

    public JoinSustain(JoinPress press) {
        this.press = press;
    }

    @Override
    public void run(ValueRuntime runtime) {
        // TODO implement
    }

    @Override
    public String getDescription() {
        return "SUSTAIN JOIN";
    }
}
