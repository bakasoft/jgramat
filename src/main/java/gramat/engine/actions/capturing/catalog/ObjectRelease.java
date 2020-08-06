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
        if (press.active) {
            var assembler = context.popAssembler();

            var object = assembler.getAttributes();  // TODO add types

            context.peekAssembler().pushValue(object);

            press.active = false;
        }
    }

    @Override
    public String getDescription() {
        return "RELEASE OBJECT";
    }
}
