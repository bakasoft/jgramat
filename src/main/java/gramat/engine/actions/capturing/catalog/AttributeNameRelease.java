package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.CapturingSubAction;

public class AttributeNameRelease extends CapturingSubAction<AttributeNamePress> {

    public AttributeNameRelease(AttributeNamePress origin) {
        super(origin);
    }

    @Override
    public final void run(CapturingContext context) {
        var accept = context.tryPostpone(AttributeNameAccept.class);

        if (accept == null) {
            context.enqueue(new AttributeNameAccept(origin));
        }
    }

    @Override
    public String getDescription() {
        return "RELEASE ATTRIBUTE NAME";
    }
}
