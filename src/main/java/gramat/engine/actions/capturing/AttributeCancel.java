package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class AttributeCancel extends ValueAction {

    private final String name;
    private final AttributePress press;

    public AttributeCancel(String name, AttributePress press) {
        this.name = name;
        this.press = press;
    }

    @Override
    public void run(ValueRuntime runtime) {
        runtime.popAssembler();
    }

    @Override
    public String getDescription() {
        return "CANCEL ATTRIBUTE: " + name;
    }
}
