package gramat.automata.raw;

import gramat.automata.ndfa.NMachineBuilder;

abstract public class RawAutomaton implements NMachineBuilder {

    abstract public RawAutomaton collapse();

}