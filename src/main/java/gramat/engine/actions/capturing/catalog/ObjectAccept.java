package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.ValueAssembler;

public class ObjectAccept extends AbstractContainerAccept {

    public ObjectAccept(AbstractContainerPress origin) {
        super(origin);
    }

    protected void processContent(CapturingContext context, ValueAssembler assembler) {
        var object = assembler.getAttributes();  // TODO add types

        context.peekAssembler().pushValue(object);
    }

    @Override
    public String getDescription() {
        return "ACCEPT OBJECT";
    }
}
