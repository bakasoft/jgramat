package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class ObjectCancel extends ValueAction {

    private final ObjectPress press;

    public ObjectCancel(ObjectPress press) {
        this.press = press;
    }

    @Override
    public void run(ValueRuntime runtime) {
        if (press.active) {
            runtime.popAssembler();
            press.active = false;
        }
    }

    @Override
    public String getDescription() {
        return "CANCEL OBJECT";
    }
}
