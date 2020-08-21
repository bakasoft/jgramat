package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.CapturingSubAction;

public class ObjectAccept extends CapturingSubAction<ObjectPress> {

    public ObjectAccept(ObjectPress origin) {
        super(origin);
    }

    public final void run(CapturingContext context) {
        var assembler = context.popAssembler();
        var object = assembler.getAttributes();  // TODO add types

        context.peekAssembler().pushValue(object);
    }

    @Override
    public String getDescription() {
        return "ACCEPT OBJECT";
    }
}
