package gramat.automata.ndfa.hooks;

import gramat.automata.ndfa.NMachine;
import gramat.automata.ndfa.NState;

public interface RecursiveHook {

    void run(NMachine machine, NState initial, NState accepted);

}
