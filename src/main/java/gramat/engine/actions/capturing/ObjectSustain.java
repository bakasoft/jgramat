package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class ObjectSustain extends ValueAction {

    private final ObjectPress press;

    public ObjectSustain(ObjectPress press) {
        this.press = press;
    }

    @Override
    public void run(ValueRuntime runtime) {
        // TODO
    }

    @Override
    public String getDescription() {
        return "SUSTAIN OBJECT";
    }
}
