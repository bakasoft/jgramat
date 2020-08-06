package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class ValueSustain extends ValueAction {

    private final ValuePress press;

    public ValueSustain(ValuePress press) {
        this.press = press;
    }

    @Override
    public void run(ValueRuntime runtime) {
        // TODO
    }

    @Override
    public String getDescription() {
        return "SUSTAIN VALUE";
    }
}
