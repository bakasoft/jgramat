package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class AttributeRelease extends ValueAction {

    private final String name;
    private final AttributePress press;

    public AttributeRelease(String name, AttributePress press) {
        this.name = name;
        this.press = press;
    }

    @Override
    public void run(ValueRuntime runtime) {
        var assembler = runtime.popAssembler();

        var value = assembler.popValue();

        assembler.expectEmpty();

        runtime.peekAssembler().setAttribute(name, value);
    }

    @Override
    public String getDescription() {
        return "RELEASE ATTRIBUTE: " + name;
    }
}
