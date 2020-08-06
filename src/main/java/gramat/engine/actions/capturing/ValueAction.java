package gramat.engine.actions.capturing;

import gramat.engine.actions.Action;
import gramat.expressions.capturing.ValueRuntime;

abstract public class ValueAction extends Action {

    abstract public void run(ValueRuntime runtime);

}
