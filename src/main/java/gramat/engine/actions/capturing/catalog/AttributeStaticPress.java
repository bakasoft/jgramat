package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class AttributeStaticPress extends CapturingAction {

    private final String name;

    public AttributeStaticPress(String name) {
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

    @Override
    public final int getOrder() {
        return 1;
    }

}
