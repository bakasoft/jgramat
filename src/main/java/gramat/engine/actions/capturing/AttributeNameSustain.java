package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class AttributeNameSustain extends ValueAction {

    private final AttributeNamePress press;

    public AttributeNameSustain(AttributeNamePress press) {
        this.press = press;
    }

    @Override
    public void run(ValueRuntime runtime) {
        // TODO
    }

    @Override
    public String getDescription() {
        return "SUSTAIN ATTRIBUTE NAME";
    }
}
