package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class AttributeValueRelease extends ValueAction {

    private final AttributeValuePress press;

    private final AttributeNameRelease releasedName;


    public AttributeValueRelease(AttributeValuePress press, AttributeNameRelease releasedName) {
        this.press = press;
        this.releasedName = releasedName;
    }

    @Override
    public void run(ValueRuntime runtime) {
        if (press.active) {
            if (releasedName.name != null) {
                var name = releasedName.name;
                var assembler = runtime.popAssembler();
                var value = assembler.popValue();

                assembler.expectEmpty();

                runtime.peekAssembler().setAttribute(name, value);
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
