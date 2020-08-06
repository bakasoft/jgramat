package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class AttributeSustain extends ValueAction {

    private final String name;
    private final AttributePress begin;

    public AttributeSustain(String name, AttributePress begin) {
        this.name = name;
        this.begin = begin;
    }

    @Override
    public void run(ValueRuntime runtime) {
        // TODO
    }

    @Override
    public String getDescription() {
        return "SUSTAIN ATTRIBUTE: " + name;
    }
}
