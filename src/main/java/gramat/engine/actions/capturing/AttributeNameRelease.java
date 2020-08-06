package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class AttributeNameRelease extends ValueAction {

    private final AttributeNamePress press;

    public String name;

    public AttributeNameRelease(AttributeNamePress press) {
        this.press = press;
    }

    @Override
    public void run(ValueRuntime runtime) {
        if (press.active) {
            var assembler = runtime.popAssembler();

            name = assembler.popString();

            assembler.expectEmpty();

            press.active = false;
        }
    }

    @Override
    public String getDescription() {
        return "RELEASE ATTRIBUTE NAME";
    }
}
