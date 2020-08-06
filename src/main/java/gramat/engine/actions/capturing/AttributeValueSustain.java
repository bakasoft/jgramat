package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class AttributeValueSustain extends ValueAction {

    private final AttributeValuePress press;

    public AttributeValueSustain(AttributeValuePress press) {
        this.press = press;
    }

    @Override
    public void run(ValueRuntime runtime) {
        // TODO
    }

    @Override
    public String getDescription() {
        return "SUSTAIN ATTRIBUTE VALUE";
    }
}
