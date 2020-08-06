package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class AttributeNameCancel extends ValueAction {

    private final AttributeNamePress press;

    public AttributeNameCancel(AttributeNamePress press) {
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
        return "CANCEL ATTRIBUTE NAME";
    }
}
