package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class ObjectRelease extends CapturingAction {

    private final ObjectPress press;

    public ObjectRelease(ObjectPress press) {
        this.press = press;
    }

    @Override
    public void run(CapturingContext context) {
        var accept = context.tryPostpone(ObjectAccept.class);

        if (accept == null) {
            if (context.present.removeLast(ObjectReject.class) == null) {
                throw new RuntimeException("expected reject");
            }

            context.future.enqueue(new ObjectAccept(press));
        }
    }

    @Override
    public String getDescription() {
        return "RELEASE OBJECT";
    }
}
