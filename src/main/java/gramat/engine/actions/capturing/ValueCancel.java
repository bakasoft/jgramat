package gramat.engine.actions.capturing;

import gramat.expressions.capturing.ValueRuntime;

public class ValueCancel extends ValueAction {

    private final ValuePress press;

    public ValueCancel(ValuePress press) {
        this.press = press;
    }

    @Override
    public void run(ValueRuntime runtime) {
        if (press.position == null) {
            System.out.println("WARNING: canceling before start @ " + this);
        }

        press.position = null;
    }

    @Override
    public String getDescription() {
        return "CANCEL VALUE";
    }
}
