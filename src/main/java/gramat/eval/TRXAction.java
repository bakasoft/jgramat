package gramat.eval;

import gramat.epsilon.Action;

abstract public class TRXAction extends Action {

    abstract public void run(Evaluator evaluator);

    abstract public String getDescription();

    @Override
    public String toString() {
        return getDescription() + " (#" + id + ")";
    }
}
