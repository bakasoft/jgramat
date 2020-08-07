package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.ValueAssembler;

public class AttributeNameAccept extends AbstractContainerAccept {

    public AttributeNameAccept(AbstractContainerPress origin) {
        super(origin);
    }

    @Override
    protected void processContent(CapturingContext context, ValueAssembler assembler) {
        var name = assembler.popString();

        assembler.expectEmpty();

        context.enqueue(new AttributeNameBaton(origin, name));
    }

    @Override
    public String getDescription() {
        return "CANCEL ATTRIBUTE NAME";
    }

    @Override
    public final int getOrder() {
        return 5;
    }
}
