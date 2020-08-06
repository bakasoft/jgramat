package gramat.engine.actions.capturing.catalog;

import gramat.engine.actions.capturing.CapturingAction;
import gramat.engine.actions.capturing.CapturingContext;

public class AttributeValueRelease extends CapturingAction {

    private final AttributeValuePress press;

    private final AttributeNameRelease releasedName;


    public AttributeValueRelease(AttributeValuePress press, AttributeNameRelease releasedName) {
        this.press = press;
        this.releasedName = releasedName;
    }

    @Override
    public void run(CapturingContext context) {
        if (press.active) {
            if (releasedName.name != null) {
                var name = releasedName.name;
                var assembler = context.popAssembler();
                var value = assembler.popValue();

                assembler.expectEmpty();

                context.peekAssembler().setAttribute(name, value);
            } else {
                System.out.println("Missing name");
            }

            press.active = false;
        }
    }

    @Override
    public String getDescription() {
        return "RELEASE ATTRIBUTE VALUE";
    }
}
