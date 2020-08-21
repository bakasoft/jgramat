package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.CapturingSubAction;

public class ObjectRelease extends CapturingSubAction<ObjectPress> {

    public ObjectRelease(ObjectPress origin) {
        super(origin);
    }

    @Override
    public final void run(CapturingContext context) {
        var accept = context.tryPostpone(ObjectAccept.class);

        if (accept == null) {
            context.enqueue(new ObjectAccept(origin));
        }
    }

    @Override
    public String getDescription() {
        return "RELEASE OBJECT";
    }
}
