package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class AttributeValueCancel extends ValueAction {

    private final AttributeValuePress press;

    public AttributeValueCancel(AttributeValuePress press) {
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
        return "CANCEL ATTRIBUTE VALUE";
    }
}
