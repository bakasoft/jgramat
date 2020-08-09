package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.ValueAssembler;

public class AttributeValueAccept extends AbstractContainerAccept {

    private final String name;

    public AttributeValueAccept(AbstractContainerPress origin, String name) {
        super(origin);
        this.name = name;
    }

    @Override
    protected void processContent(CapturingContext context, ValueAssembler assembler) {
        var value = assembler.popValue();

        assembler.expectEmpty();

        context.peekAssembler().setAttribute(name, value);
    }

    @Override
    public String getDescription() {
        return "ACCEPT ATTRIBUTE VALUE";
    }

    @Override
    public final int getOrder() {
        return 11;
    }
}
