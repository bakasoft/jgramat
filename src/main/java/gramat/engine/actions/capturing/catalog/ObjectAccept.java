package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class ObjectAccept extends CapturingAction {

    private final ObjectPress press;

    public ObjectAccept(ObjectPress press) {
        this.press = press;
    }

    @Override
    public void run(CapturingContext context) {
        var assembler = context.popAssembler();

        var object = assembler.getAttributes();  // TODO add types

        context.peekAssembler().pushValue(object);
    }

    @Override
    public String getDescription() {
        return "RELEASE OBJECT";
    }
}
