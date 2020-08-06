package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class AttributePress extends ValueAction {

    private final String name;

    public AttributePress(String name) {
        this.name = name;
    }

    @Override
    public void run(ValueRuntime runtime) {
        runtime.pushAssembler();
    }

    @Override
    public String getDescription() {
        return "PRESS ATTRIBUTE: " + name;
    }

}
