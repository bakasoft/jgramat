package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.ValueAssembler;
import gramat.tools.Debug;

public class AttributeNameAccept extends AbstractContainerAccept {

    public AttributeNameAccept(AbstractContainerPress origin) {
        super(origin);
    }

    @Override
    protected void beforeProcessContent(CapturingContext context) {
        var accept = context.dequeue(ValueAccept.class);

        if (accept != null) {
            Debug.log("Execute action: " + accept);

            accept.run(context);
        }
    }

    @Override
    protected void processContent(CapturingContext context, ValueAssembler assembler) {
        var name = assembler.popString();

        assembler.expectEmpty();

        context.enqueue(new AttributeNameBaton(origin, name));
    }

    @Override
    public String getDescription() {
        return "ACCEPT ATTRIBUTE NAME";
    }
}
