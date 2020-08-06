package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class ObjectPress extends ValueAction {

    // TODO add comment why this is active by default
    public boolean active = true;

    @Override
    public void run(ValueRuntime runtime) {
        if (!active) {
            runtime.pushAssembler();
            active = true;
        }
    }

    @Override
    public String getDescription() {
        return "PRESS OBJECT";
    }

}
