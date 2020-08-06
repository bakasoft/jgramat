package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class ValuePress extends ValueAction {

    public Integer position;

    @Override
    public void run(ValueRuntime runtime) {
        if (position != null) {
            System.out.println("WARNING: re-starting @ " + this);
        }

        position = runtime.input.getPosition();
    }

    @Override
    public String getDescription() {
        return "PRESS VALUE";
    }
}
