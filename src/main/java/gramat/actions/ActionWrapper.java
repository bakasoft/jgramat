package gramat.actions;

import gramat.util.Chain;

public interface ActionWrapper {  // TODO make it an object and use it with composition

    Chain<Action> getBegin();
    Chain<Action> getEnd();

}
