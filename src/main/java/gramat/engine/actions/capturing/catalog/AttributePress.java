package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class AttributePress extends CapturingAction {

    private final String name;

    public AttributePress(String name) {
        this.name = name;
    }

    @Override
    public void run(CapturingContext context) {
        context.pushAssembler();
    }

    @Override
    public String getDescription() {
        return "PRESS ATTRIBUTE: " + name;
    }

}
