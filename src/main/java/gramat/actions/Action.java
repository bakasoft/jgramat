package gramat.actions;

import gramat.eval.Context;

public abstract class Action {  // TODO make it interface

    abstract public void run(Context context);

    @Override
    abstract public String toString();

}
