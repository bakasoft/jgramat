package gramat.automata.ndfa.hooks;

import gramat.automata.ndfa.NState;

public interface StateHook {

    void run(NState state);

}
