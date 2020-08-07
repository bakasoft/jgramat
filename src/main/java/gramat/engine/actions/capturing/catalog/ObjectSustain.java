package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class ObjectSustain extends CapturingAction {

    private final ObjectPress press;

    public ObjectSustain(ObjectPress press) {
        this.press = press;
    }

    @Override
    public void run(CapturingContext context) {
        if (context.tryPostpone(ObjectReject.class) == null) {
            throw new RuntimeException("expected reject to be posposed");
        }
    }

    @Override
    public String getDescription() {
        return "SUSTAIN OBJECT";
    }
}
