package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingContext;
import gramat.engine.actions.capturing.CapturingSubAction;

public class AttributeValueAccept extends CapturingSubAction<AttributeValuePress> {

    private final String name;

    public AttributeValueAccept(AttributeValuePress origin, String name) {
        super(origin);
        this.name = name;
    }

    @Override
    public final void run(CapturingContext context) {
        var assembler = context.popAssembler();
        var value = assembler.popValue();

        assembler.expectEmpty();

        context.peekAssembler().setAttribute(name, value);
    }

    @Override
    public String getDescription() {
        return "ACCEPT ATTRIBUTE VALUE";
    }
}
