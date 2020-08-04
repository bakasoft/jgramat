package gramat.engine;

import gramat.engine.checks.ControlStack;

public interface Runner {

    char getChar();

    ControlStack getControlStack();

}
