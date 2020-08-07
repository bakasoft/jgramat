package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class AttributeStaticRelease extends CapturingAction {

    private final String name;
    private final AttributeStaticPress press;

    public AttributeStaticRelease(String name, AttributeStaticPress press) {
        this.name = name;
        this.press = press;
    }

    @Override
    public void run(CapturingContext context) {
        var assembler = context.popAssembler();

        var value = assembler.popValue();

        assembler.expectEmpty();

        context.peekAssembler().setAttribute(name, value);
    }

    @Override
    public String getDescription() {
        return "RELEASE ATTRIBUTE: " + name;
    }
}
