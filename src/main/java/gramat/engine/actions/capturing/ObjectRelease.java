package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class ObjectRelease extends ValueAction {

    private final ObjectPress press;

    public ObjectRelease(ObjectPress press) {
        this.press = press;
    }

    @Override
    public void run(ValueRuntime runtime) {
        if (press.active) {
            var assembler = runtime.popAssembler();

            var object = assembler.getAttributes();  // TODO add types

            runtime.peekAssembler().pushValue(object);

            press.active = false;
        }
    }

    @Override
    public String getDescription() {
        return "RELEASE OBJECT";
    }
}
