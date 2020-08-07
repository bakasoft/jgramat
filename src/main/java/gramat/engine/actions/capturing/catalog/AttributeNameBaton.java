package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.CapturingSubAction;

public class AttributeNameBaton extends CapturingSubAction<AbstractContainerPress> {

    public final String name;

    public AttributeNameBaton(AbstractContainerPress origin, String name) {
        super(origin);
        this.name = name;
    }

    @Override
    public void run(CapturingContext context) {
        // TODO
    }

    @Override
    public String getDescription() {
        return "ATTRIBUTE NAME BATON";
    }

    @Override
    public final int getOrder() {
        return 6;
    }
}
